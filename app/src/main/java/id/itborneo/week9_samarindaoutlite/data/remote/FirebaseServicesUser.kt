package id.itborneo.week9_samarindaoutlite.data.remote

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.data.model.user.Customer


class FirebaseServiceUser(private val activity: Activity) {

    private val TAG = "FirebaseRemote"

    private val mAuth = FirebaseApi.getAuth()
    private val mDatabase = FirebaseApi.getDatabaseRef()
    private val mCustomer = mDatabase.child("user").child("customer")
    private val mAdmin = mDatabase.child("user").child("admin")

    fun addCustomer(user: Customer): LiveData<Int> {
        val result = MutableLiveData<Int>()
        val uid = mAuth.currentUser?.uid
        if (uid != null) {
            mCustomer.child(uid).setValue(user).addOnCompleteListener {
                result.postValue(1)
            }.addOnCanceledListener {
                result.postValue(0)
            }
        }
        return result
    }


    fun addAdmin(admin: Admin): LiveData<Int> {
        val result = MutableLiveData<Int>()

        val uid = mAuth.currentUser?.uid
        if (uid != null) {
            mAdmin.child(uid).setValue(admin)
                .addOnCompleteListener {
                result.postValue(1)
            }.addOnCanceledListener {
                result.postValue(0)
            }
        }
        return result

    }


    fun getAuth() =
        mAuth

    fun registerUserAuth(email: String, pass: String): LiveData<Task<AuthResult>> {

        val task = MutableLiveData<Task<AuthResult>>()

        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(activity) { taskAuth ->

                task.postValue(taskAuth)

            }
        return task
    }


    fun login(email: String, pass: String): LiveData<Task<AuthResult>> {

        val task = MutableLiveData<Task<AuthResult>>()

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(activity) { taskAuth ->

                task.postValue(taskAuth)

            }

        return task
    }


    fun logOut(): LiveData<Int> {
        Log.d(TAG, "logOut")
        val result = MutableLiveData<Int>()


        getAuth().addAuthStateListener {
            if (getAuth().currentUser == null) {
                result.postValue(1)
            } else {
                result.postValue(0)
            }
        }

        getAuth().signOut()

        return result
    }

    fun getUserAdmin(): LiveData<Admin> {
        Log.d(TAG, "getUserAdmin: called")

        val result = MutableLiveData<Admin>()
        val uid = mAuth.uid ?: return result
        mAdmin.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "getUserAdmin: onDataChange ${snapshot.value}")

                result.postValue(snapshot.getValue(Admin::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "getUserAdmin: onCancelled")
                result.postValue(null)

            }

        })
        return result
    }

    fun getUserCustomer(): LiveData<Customer> {
        Log.d(TAG, "getUserCustomer: called")

        val result = MutableLiveData<Customer>()
        val uid = mAuth.uid ?: return result
        mCustomer.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "getUserCustomer: onDataChange ${snapshot.value}")

                result.postValue(snapshot.getValue(Customer::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "getUserCustomer: onCancelled")
                result.postValue(null)

            }

        })
        return result
    }
}
