package kr.euicheon.leejungpyo.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.YearMonth

/**
 * Contains metadata information about a certain day
 *
 * @property day Indicates the day number (1-31)
 *
 * @property dayOfWeek Contains DayOfWeek information about the day (Monday-Sunday)
 *
 * @property day Information about the month the date belongs to
 *
 */
data class LeeDate(
    val day: Int,
    val dayOfWeek: DayOfWeek,
    val month: YearMonth,
    val userId: String? = null,
    val todoList: List<String>? = null,
    val dayImage: String? = null,
    val dateId: String? = null

) :Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        DayOfWeek.of(parcel.readInt()),
        YearMonth.parse(parcel.readString() ?: "2023-10"),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(day)
        parcel.writeString(userId)
        parcel.writeStringList(todoList)
        parcel.writeString(dayImage)
        parcel.writeString(dateId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LeeDate> {
        override fun createFromParcel(parcel: Parcel): LeeDate {
            return LeeDate(parcel)
        }

        override fun newArray(size: Int): Array<LeeDate?> {
            return arrayOfNulls(size)
        }
    }
}