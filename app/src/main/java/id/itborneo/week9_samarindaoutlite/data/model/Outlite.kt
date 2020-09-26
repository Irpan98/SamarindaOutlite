package id.itborneo.week9_samarindaoutlite.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OutliteModel(
    var name: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    var id: String = ""
) : Parcelable