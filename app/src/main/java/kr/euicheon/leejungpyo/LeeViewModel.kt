package kr.euicheon.leejungpyo


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kr.euicheon.leejungpyo.data.Event
import kr.euicheon.leejungpyo.data.UserData
import java.util.Date
import javax.inject.Inject

const val USERS = "users"

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
            UserData(        //userData 라는 곳에 유저 정보 저장 Parameter에 입력된 정보가 있으면 해당 정보 저장, 없으면 기존 정보 저장
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
            }
            .addOnFailureListener { exc ->
                handleException(exc, "사용자 정보를 가져오지 못했습니다 :(")
                inProgress.value = false
            }

    }

    private fun onCreateToDo() {

    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage \n : $errorMsg "
        popupNotification.value = Event(message)

    }

}
