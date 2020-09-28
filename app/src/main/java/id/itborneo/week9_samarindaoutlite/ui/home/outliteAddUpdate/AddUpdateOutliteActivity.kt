package id.itborneo.week9_samarindaoutlite.ui.home.outliteAddUpdate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import id.itborneo.week9_samarindaoutlite.ui.mapMarker.MapMarkerActivity
import id.itborneo.week9_samarindaoutlite.ui.mapMarker.MapMarkerActivity.Companion.EXTRA_FROM_ADD_UPDATE_OUTLITE_FRAGMENT
import id.itborneo.week9_samarindaoutlite.ui.mapMarker.MapMarkerActivity.Companion.REQ_MAP
import id.itborneo.week9_samarindaoutlite.utils.EXTRA_OUTLITE
import id.itborneo.week9_samarindaoutlite.utils.SpinKitUtils
import id.itborneo.week9_samarindaoutlite.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_create_update_outlite.*

class AddUpdateOutliteActivity : AppCompatActivity() {

    private val TAG = "AddUpdateOutliteActivity"
    private var outlite = OutliteModel()
    private lateinit var viewModel: AddUpdateOutliteViewModel

    companion object {
        private const val REQ_STATUS_ADD = "add"
        private const val REQ_STATUS_UPDATE = "update"

    }

    private lateinit var reqStat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_outlite)

        initViewModel()
        getIntentData()
        initGetLocationListener()
        reqChecker()
        initAddUpdateListener()
        updateUI()

    }

    private fun getIntentData() {
        val dataIntent = intent.getParcelableExtra<OutliteModel>(
            EXTRA_OUTLITE
        ) ?: return

        outlite = dataIntent
        updateUI()
    }

    private fun loading(showLoading: Boolean) {
        if (showLoading) {
            SpinKitUtils.show(spinKitLoading)
        } else {
            SpinKitUtils.hide(spinKitLoading)
        }
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[AddUpdateOutliteViewModel::class.java]

    }


    private fun initGetLocationListener() {
        btnGetLocation.setOnClickListener {
            setOutliteName()
            actionToMapsMarkerActivity()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {

        val lat = outlite.latitude
        val lng = outlite.longitude

        etLocation.setText(outlite.location)
        if (lat != 0.0 || lng != 0.0) etLatLng.setText("${outlite.latitude} , ${outlite.longitude}")
        etName.setText(outlite.name)

    }

    private fun initAddUpdateListener() {


        btnAddUpdate.setOnClickListener {
            if (isInputEmpty()) return@setOnClickListener

            loading(true)

            setOutliteName()

            actionAddUpdate()

        }
    }

    private fun reqChecker() {
        reqStat = if (outlite.id.isBlank()) {
            REQ_STATUS_ADD
        } else {
            REQ_STATUS_UPDATE
        }
    }

    private fun actionAddUpdate() {


        when (reqStat) {
            REQ_STATUS_ADD -> {
                Log.d(TAG, "reqStat: REQ_STATUS_ADD")

                viewModel.addOutlite(outlite).observe(this) {
                    success()
                }
            }
            REQ_STATUS_UPDATE -> {
                Log.d(TAG, "reqStat: REQ_STATUS_UPDATE")
                viewModel.updateOutlite(outlite).observe(this) {
                    success()
                }
            }
            else -> return
        }


    }


    private fun isInputEmpty(): Boolean {

        val tidakBoleh = "tidak boleh kosong"

        val isOutliteNull = ViewUtils.validateInput(etName, "Outlite $tidakBoleh")
        val isLocationNull = ViewUtils.validateInput(etLocation, "Location $tidakBoleh")

        if (isLocationNull) {
            Toast.makeText(this, "Klik Tombol Location", Toast.LENGTH_SHORT).show()
        }

        return isOutliteNull ||
                isLocationNull

    }

    private fun setOutliteName() {
        outlite.name = etName.text.toString()

    }

    private fun success() {
        loading(false)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun actionToMapsMarkerActivity() {
        val intent = Intent(this, MapMarkerActivity::class.java)
        intent.putExtra(EXTRA_FROM_ADD_UPDATE_OUTLITE_FRAGMENT, this.javaClass.simpleName)
        intent.putExtra(EXTRA_OUTLITE, outlite)
        startActivityForResult(intent, REQ_MAP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_MAP) {
                if (data == null) return
                val intentOutlite = data.getParcelableExtra<OutliteModel>(EXTRA_OUTLITE) ?: return
                outlite = intentOutlite
                updateUI()
            }
        }
    }

}