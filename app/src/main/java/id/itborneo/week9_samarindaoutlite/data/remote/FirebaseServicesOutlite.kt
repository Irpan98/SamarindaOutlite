package id.itborneo.week9_samarindaoutlite.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel


class FirebaseServicesOutlite {
    private val mDatabase = FirebaseApi.getDatabaseRef()

    private val TAG = "FirebaseServicesOutlite"
    private val OutliteChild = mDatabase.child("outlite")

    fun addOutlite(outlite: OutliteModel): LiveData<Int> {

        var response = MutableLiveData<Int>()

        Log.d(TAG, "addOutlite called")
        OutliteChild.push().setValue(
            outlite
        ).addOnSuccessListener {
            response.postValue(1)

        }.addOnFailureListener {
            Log.d("FirebaseServicesOutlite", "addOnFailureListener ${it.message}")
            response.postValue(0)
        }

        return response

    }

    fun updateOutlite(outlite: OutliteModel): LiveData<Int> {

        val response = MutableLiveData<Int>()

        Log.d(TAG, "addOutlite called")
        OutliteChild.child(outlite.id).setValue(outlite)
            .addOnSuccessListener {
                response.postValue(1)

            }.addOnFailureListener {
                Log.d("FirebaseServicesOutlite", "addOnFailureListener ${it.message}")
                response.postValue(0)
            }

        return response

    }

    fun deleteOutlite(key: String): LiveData<Int> {

        val response = MutableLiveData<Int>()

        Log.d(TAG, "deleteOutlite called")
        OutliteChild.child(key).setValue(null)
            .addOnSuccessListener {
                response.postValue(1)

            }.addOnFailureListener {
                Log.d("FirebaseServicesOutlite", "addOnFailureListener ${it.message}")
                response.postValue(0)
            }

        return response

    }

    fun getAllOutlite(): MutableLiveData<List<OutliteModel>> {
        Log.d(TAG, "getAllOutlite called")

        val outlite = MutableLiveData<List<OutliteModel>>()


        OutliteChild.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val listOutlite = mutableListOf<OutliteModel>()

                val dataSnapshot = snapshot.children
                dataSnapshot.forEach {

                    var outliteSnapshot = it.getValue(OutliteModel::class.java)

                    if (outliteSnapshot != null) {
                        outliteSnapshot.id = it.key ?: ""

                        listOutlite.add(outliteSnapshot)
                    }
                    Log.d(TAG, outliteSnapshot.toString())
                }
                outlite.postValue(listOutlite)


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled called : ${error.message}")
            }

        })


        return outlite
    }
}