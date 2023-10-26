package kr.euicheon.leejungpyo.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.YearMonth

data class CalendarDate(
    val day: Int,
    val dayOfWeek: DayOfWeek,
    val month: YearMonth,
    val dayImage: String? = null
) :Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        DayOfWeek.of(parcel.readInt()),
        YearMonth.parse(parcel.readString() ?: "2023-10"),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(day)
        parcel.writeString(dayImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarDate> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFromParcel(parcel: Parcel): CalendarDate {
            return CalendarDate(parcel)
        }

        override fun newArray(size: Int): Array<CalendarDate?> {
            return arrayOfNulls(size)
        }
    }
}