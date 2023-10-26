package kr.euicheon.leejungpyo


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kr.euicheon.leejungpyo.data.CalendarDate
import kr.euicheon.leejungpyo.data.Event
import kr.euicheon.leejungpyo.data.LeeDate
import kr.euicheon.leejungpyo.data.UserData
import java.io.FileDescriptor
import java.time.YearMonth
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject

const val USERS = "users"
const val DATECOMP = "leedate"

@HiltViewModel
class LeeViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage,
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf<Event<String>?>(null)
    val dateComponent = mutableStateOf<LeeDate?>(null)
    val dateComponentList =mutableStateOf<List<LeeDate>>(listOf())



    init {
        //auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    fun onSignup(username: String, email: String, pass: String) {
        if (username.isEmpty() or email.isEmpty() or pass.isEmpty()) {
            handleException(customMessage = "모든 칸을 채워주세요!")
            return
        }
        inProgress.value = true

        db.collection(USERS).whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {     // Firestore 내부에 'username'이 이미 저장되어 있는지 확인.
                    handleException(customMessage = "이미 존재하는 회원 이름입니다 :)")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, pass)    // 저장되어 있지 않으면 auth에 create
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "회원가입 실패 :(")
                            }
                            inProgress.value = false
                        }
                }
            }
            .addOnFailureListener { }
    }

    fun onLogin(email: String, pass: String) {
        if (email.isEmpty() or pass.isEmpty()) {
            handleException(customMessage = "모든 칸을 채워주세요!")
            return
        }
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let { uid ->
                        handleException(customMessage = "로그인 성공 :)")
                        getUserData(uid)
                    }
                } else {
                    handleException(task.exception, "로그인 실패 :(")
                    inProgress.value = false
                }
            }
            .addOnFailureListener { exc ->
                handleException(exc, "로그인 실패 :(")
                inProgress.value = false
            }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
    ) {
        val uid = auth.currentUser?.uid
        val userData =
            UserData(
                //userData 라는 곳에 유저 정보 저장 Parameter에 입력된 정보가 있으면 해당 정보 저장, 없으면 기존 정보 저장
                userId = uid,
                name = name ?: userData.value?.name,
                username = username ?: userData.value?.username,
            )

        uid?.let {
            inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                inProgress.value = false
                            }
                            .addOnFailureListener {
                                handleException(it, "사용자 갱신에 실패하였습니다 :( ")
                                inProgress.value = false
                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        inProgress.value = false
                    }
                }
                .addOnFailureListener { exc ->
                    handleException(exc, "사용자 생성에 실패하였습니다 :( ")
                    inProgress.value = false
                }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                inProgress.value = false
                refreshToDo()
            }
            .addOnFailureListener { exc ->
                handleException(exc, "사용자 정보를 가져오지 못했습니다 :(")
                inProgress.value = false
            }

    }

    fun getCalendarData(dayDate: CalendarDate) {
        val day = dayDate.day
        val month = dayDate.month

        db.collection(DATECOMP).whereEqualTo("day", day).whereEqualTo("month", month).get()
            .addOnSuccessListener { querySnapshot ->
                if(querySnapshot.documents.isNotEmpty()) {
                    val singleDocument = querySnapshot.documents[0]
                    val leeDate = singleDocument.toObject(LeeDate::class.java)
                    if(leeDate != null)
                     dateComponent.value = leeDate
                } else {
                    handleException(customMessage = "No Data :(")
                }
            }
            .addOnFailureListener { exc->
                handleException(exc, "데이터를 가져오는데 실패했습니다 :(")
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onCreateToDo(description: List<String>, dayDate: CalendarDate) {
        val currentUid = auth.currentUser?.uid

        if (currentUid != null) {
            val dateUuid = UUID.randomUUID().toString()

            // Firebase에 저장할 데이터 셋.
            val leeDate = LeeDate(
                day = dayDate.day,
                dayOfWeek = dayDate.dayOfWeek,
                month = dayDate.month,
                userId = currentUid,
                todoList = description,
                dateId = dateUuid
            )

            db.collection(DATECOMP).document(dateUuid).set(leeDate)
                .addOnSuccessListener {
                    popupNotification.value = Event("저장되었습니다 :)")
                    refreshToDo()
                }
        }

    }

    private fun refreshToDo() {
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            db.collection(DATECOMP).whereEqualTo("userId", currentUid).get()
                .addOnSuccessListener { documents ->
                    convertToDo(documents, dateComponentList)
                }
                .addOnFailureListener { exc ->
                    handleException(exc, "일정을 가져오는데 실패했어요 :(")
                }
        }
    }

    private fun convertToDo(documents: QuerySnapshot, outState: MutableState<List<LeeDate>>) {
        val newToDo = mutableListOf<LeeDate>()
        documents.forEach { doc ->
            val todo = doc.toObject<LeeDate>()
            newToDo.add(todo)
        }

        outState.value = newToDo

    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage \n : $errorMsg "
        popupNotification.value = Event(message)

    }

}
