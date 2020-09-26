package id.itborneo.week9_samarindaoutlite.ui.home.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import id.itborneo.week9_samarindaoutlite.ui.home.HomeViewModel
import id.itborneo.week9_samarindaoutlite.utils.MapUtils

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var mapUtils: MapUtils
    private lateinit var viewModel: HomeViewModel

    private lateinit var outlites: List<OutliteModel>
    private val TAG = this.javaClass.name
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getOutlitesData()
        initMapFragment()
    }

    private fun getOutlitesData() {
        viewModel.getAllOutlite().observe(requireActivity()) {
            outlites = it
            setAllMarker()
        }
    }


    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[HomeViewModel::class.java]
        } ?: throw Exception("Salah Activity")
    }

    private fun initMapFragment() {
        val mapFragment =
            this.childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.d(TAG, "MapUtils")

        map = (p0 ?: return)
        Log.d(TAG, "MapUtils 2")
        mapUtils = MapUtils(requireContext(), map)

        mapUtils.initMapMarker()
    }

    private fun setAllMarker() {
        val latLngs = mutableListOf<LatLng>()
        val locations = mutableListOf<String>()
        outlites.forEach {
            latLngs.add(
                LatLng(it.latitude, it.longitude)
            )
            locations.add(it.name)

        }
        mapUtils.initMapMarkers(latLngs, locations, R.drawable.ic_outlite)

    }
}