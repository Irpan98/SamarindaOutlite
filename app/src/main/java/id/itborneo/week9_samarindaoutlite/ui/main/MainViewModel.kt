package id.itborneo.week9_samarindaoutlite.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import id.itborneo.week9_samarindaoutlite.data.OutliteRepository
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.data.model.user.Customer
import id.itborneo.week9_samarindaoutlite.data.remote.FirebaseServiceUser

class MainViewModel : ViewModel() {

    val mainActivity = MainActivity()
    private val firebaseRemote = FirebaseServiceUser(mainActivity)
    private val firebaseAuthentication = firebaseRemote.getAuth()

    private val repository = OutliteRepository()

    fun register(email: String, pass: String): LiveData<Task<AuthResult>> {
        return firebaseRemote.registerUserAuth(email, pass)
    }

    fun login(email: String, pass: String) =
        repository.loginUser(mainActivity, email, pass)

    fun insertCustomer(customer: Customer) =
        firebaseRemote.addCustomer(customer)


    fun insertAdmin(admin: Admin) =
        firebaseRemote.addAdmin(admin)

    fun firebaseAuth(): FirebaseAuth {
        return firebaseAuthentication
    }

    fun getUserAdmin() =
        repository.getUserAdmin(mainActivity)

    fun getUserCustomer() =
        repository.getUserCustomer(mainActivity)
}