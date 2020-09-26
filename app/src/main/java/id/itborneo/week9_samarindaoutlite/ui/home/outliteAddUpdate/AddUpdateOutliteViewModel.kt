package id.itborneo.week9_samarindaoutlite.ui.home.outliteAddUpdate

import androidx.lifecycle.ViewModel
import id.itborneo.week9_samarindaoutlite.data.OutliteRepository
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel

class AddUpdateOutliteViewModel : ViewModel() {

    private val outliteRepo = OutliteRepository()

    fun addOutlite(outlite: OutliteModel) =
        outliteRepo.addOutlite(outlite)


    fun updateOutlite(outlite: OutliteModel) =
        outliteRepo.updateOutlite(outlite)

}
