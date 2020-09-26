package id.itborneo.week9_samarindaoutlite.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    val name: String ="",
    val email: String ="",
    val phoneNumber: String ="",
    val address: String ="",
    val job: String ="",
    val level: String ="",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
):Parcelable
