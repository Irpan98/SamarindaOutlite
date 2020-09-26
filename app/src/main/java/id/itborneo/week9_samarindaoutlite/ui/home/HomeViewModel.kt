package id.itborneo.week9_samarindaoutlite.ui.home

import androidx.lifecycle.ViewModel
import id.itborneo.week9_samarindaoutlite.data.OutliteRepository
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.data.model.user.Customer

class HomeViewModel : ViewModel() {
    private val activity = HomeActivity()
    private val outliteRepo = OutliteRepository()

    lateinit var userLevel: String

    lateinit var admin: Admin
    lateinit var customer: Customer
    fun getAllOutlite() =
        outliteRepo.getAllOutlite()

    fun deleteOutlite(key: String) =
        outliteRepo.deleteOutlite(key)

    fun logoutUser() =
        outliteRepo.logOutUser(activity)


}