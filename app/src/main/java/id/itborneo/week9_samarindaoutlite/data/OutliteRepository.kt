package id.itborneo.week9_samarindaoutlite.data

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import id.itborneo.week9_samarindaoutlite.data.remote.FirebaseServiceUser
import id.itborneo.week9_samarindaoutlite.data.remote.FirebaseServicesOutlite

class OutliteRepository {
    private val firebaseServicesOutlite = FirebaseServicesOutlite()


    fun addOutlite(outlite: OutliteModel) =
        firebaseServicesOutlite.addOutlite(outlite)


    fun getAllOutlite(): MutableLiveData<List<OutliteModel>> {
        return firebaseServicesOutlite.getAllOutlite()
    }

    fun updateOutlite(outlite: OutliteModel) =
        firebaseServicesOutlite.updateOutlite(outlite)

    fun deleteOutlite(key: String) =
        firebaseServicesOutlite.deleteOutlite(key)

    fun logOutUser(activity: Activity) =
        FirebaseServiceUser(activity).logOut()

    fun loginUser(activity: Activity, email: String, pass: String) =
        FirebaseServiceUser(activity).login(email, pass)

    fun getUserAdmin(activity: Activity) =
        FirebaseServiceUser(activity).getUserAdmin()

    fun getUserCustomer(activity: Activity) =
        FirebaseServiceUser(activity).getUserCustomer()
}