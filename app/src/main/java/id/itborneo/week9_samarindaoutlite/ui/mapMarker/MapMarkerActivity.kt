package id.itborneo.week9_samarindaoutlite.ui.mapMarker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import id.itborneo.week9_samarindaoutlite.ui.home.outliteAddUpdate.AddUpdateOutliteActivity
import id.itborneo.week9_samarindaoutlite.ui.main.register.RegisterFragment
import id.itborneo.week9_samarindaoutlite.utils.EXTRA_OUTLITE
import id.itborneo.week9_samarindaoutlite.utils.MapUtils
import kotlinx.android.synthetic.main.activity_map_marker.*


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var mapUtils: MapUtils
    private val TAG = "MapMarkerActivity"
    private lateinit var activityCaller: String
    private var outlite = OutliteModel()

    companion object {
        const val REQ_MAP = 20

        const val EXTRA_FROM_ADD_UPDATE_OUTLITE_FRAGMENT = "outline fragment"
        const val EXTRA_FROM_REGISTER_ACTIVITY = "register activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_marker)
        supportActionBar?.hide()

        getIntentData()
        initMapFragment()
        atachBtnLocationListener()
    }


    private fun getIntentData() {
        val reqStatus =
            intent.getStringExtra(
                EXTRA_FROM_ADD_UPDATE_OUTLITE_FRAGMENT
            ) ?: intent.getStringExtra(
                EXTRA_FROM_REGISTER_ACTIVITY
            )
            ?: return


        val outliteIntent = intent.getParcelableExtra<OutliteModel>(EXTRA_OUTLITE)
        if (outliteIntent != null) {
            outlite = outliteIntent
        }

        activityCaller = reqStatus
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        map = (googleMap ?: return)
        mapUtils = MapUtils(this.applicationContext, map)
        mapUtils.initMapMarker(R.drawable.ic_pin)
        mapUtils.initMarkerCameraListener { getLocation ->

            mapUtils.getAddress(getLocation) {
                outlite =
                    OutliteModel(
                        outlite.name,
                        it,
                        getLocation.latitude,
                        getLocation.longitude,
                        outlite.id
                    )

                updateUI(it)
            }

        }

    }

    private fun updateUI(address: String) {
        tvLocation.text = address

    }

    private fun initMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun atachBtnLocationListener() {
        btnGetLocation.setOnClickListener {
            success()
        }
    }

    private fun success() {

        val intent: Intent = when (activityCaller) {
            AddUpdateOutliteActivity::class.java.simpleName -> {
                Intent(baseContext, AddUpdateOutliteActivity::class.java)
            }
            RegisterFragment::class.java.simpleName -> {
                Intent(baseContext, RegisterFragment::class.java)

            }
            else -> return
        }

        intent.putExtra(EXTRA_OUTLITE, outlite)
        setResult(RESULT_OK, intent)
        finish()
    }


}