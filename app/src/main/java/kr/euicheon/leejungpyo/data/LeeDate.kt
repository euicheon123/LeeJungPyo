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
data class LeeDate (val day: Int, val dayOfWeek: DayOfWeek, val month: YearMonth): Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        DayOfWeek.of(parcel.readInt()),
        YearMonth.parse(parcel.readString() ?: "1970-01")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(day)
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