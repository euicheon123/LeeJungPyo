package kr.euicheon.leejungpyo.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class LeeDate(
    val day: Int = 0,
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val month: YearMonth = YearMonth.now(),
    val userId: String? = null,
    val todoList: List<String>? = null,
    val dayImage: String? = null,
    val dateId: String? = null

) :Parcelable {

        @RequiresApi(Build.VERSION_CODES.O)
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            DayOfWeek.of(parcel.readInt()),
            YearMonth.parse(parcel.readString() ?: "1999-03"),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(day)
            parcel.writeInt(dayOfWeek.value)  // Parcel dayOfWeek
            parcel.writeString(month.toString())  // Parcel month
            parcel.writeString(userId)
            parcel.writeStringList(todoList)
            parcel.writeString(dayImage)
            parcel.writeString(dateId)
        }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LeeDate> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFromParcel(parcel: Parcel): LeeDate {
            return LeeDate(parcel)
        }

        override fun newArray(size: Int): Array<LeeDate?> {
            return arrayOfNulls(size)
        }
    }
}


