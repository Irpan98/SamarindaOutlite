package id.itborneo.week9_samarindaoutlite.data.remote

import android.location.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseApi  {

    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabaseReference = FirebaseDatabase.getInstance().reference


    fun getAuth() =
        mAuth

    fun getDatabaseRef() =
        mDatabaseReference



}