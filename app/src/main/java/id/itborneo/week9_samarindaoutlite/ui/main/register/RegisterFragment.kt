package id.itborneo.week9_samarindaoutlite.ui.main.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.data.model.user.Customer
import id.itborneo.week9_samarindaoutlite.ui.main.MainViewModel
import id.itborneo.week9_samarindaoutlite.ui.mapMarker.MapMarkerActivity
import id.itborneo.week9_samarindaoutlite.utils.*
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    private val TAG = "RegisterFragment"
    private var levelUser = LEVEL_CUSTOMER
    private lateinit var viewModel: MainViewModel
    private lateinit var userLocation: LatLng
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initSpinner()
        initRegisterListener()
        initViewModel()
        updateUI()
        initGetLocationListener()

    }

    private fun initGetLocationListener() {
        btnGetLocation.setOnClickListener {
            actionToMapsMarkerActivity()
        }
    }

    private fun actionToMapsMarkerActivity() {
        val intent = Intent(requireContext(), MapMarkerActivity::class.java)
        intent.putExtra(
            MapMarkerActivity.EXTRA_FROM_ADD_UPDATE_OUTLITE_FRAGMENT,
            this.javaClass.simpleName
        )
        startActivityForResult(intent, MapMarkerActivity.REQ_MAP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == MapMarkerActivity.REQ_MAP) {
                if (data == null) return
                val intentOutlite = data.getParcelableExtra<OutliteModel>(EXTRA_OUTLITE) ?: return
                userLocation = LatLng(intentOutlite.latitude, intentOutlite.longitude)
                updateUI()
            }
        }
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[MainViewModel::class.java]
        } ?: throw Exception("Salah Activity")
    }

    private fun initSpinner() {
        val levels = mutableListOf(LEVEL_CUSTOMER, LEVEL_ADMIN)

        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, levels).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spLevel.setAdapter(this)
        }

        spLevel.getSpinner().onItemSelectedListener = SpinnerListener(levels) {
            levelUser = it
            updateUI()

        }

    }

    private fun initRegisterListener() {
        btnRegister.setOnClickListener {
            Log.d(TAG, " initRegisterListener spinner get $levelUser")


            if (isInputEmpty()) return@setOnClickListener

            actionRegister(etEmail.text.toString(), etPassword.text.toString())


        }
    }

    private fun isInputEmpty(): Boolean {

        val tidakBoleh = "tidak boleh kosong"

        val isEmailNull = ViewUtils.validateInput(etEmail, "Email $tidakBoleh")
        val isPassNull = ViewUtils.validateInput(etPassword, "Password $tidakBoleh")
        val isNameNull = ViewUtils.validateInput(etName, "Nama $tidakBoleh")
        val isPhoneNumberNull = ViewUtils.validateInput(etPhone, "Nomor Telepon $tidakBoleh")
        val isAddressNull = ViewUtils.validateInput(etAddress, "Alamat $tidakBoleh")

        val isJobNull = ViewUtils.validateInput(etJob, "Pekerjaan $tidakBoleh")

        val isLocationNull = ViewUtils.validateInput(etLatLng, "Location $tidakBoleh")
        if (isLocationNull) {
            Toast.makeText(requireContext(), "Tekan Location", Toast.LENGTH_SHORT).show()
        }

        return isEmailNull ||
                isPassNull ||
                isNameNull ||
                isPhoneNumberNull ||
                isAddressNull ||
                isJobNull ||
                isLocationNull
    }


    private fun actionRegister(email: String, pass: String) {
        loading(true)

        viewModel.register(email, pass).observe(requireActivity(), { task ->
            loading(false)

            if (task.isSuccessful) {
                Log.d(TAG, ": actionRegister isSuccessful")

                when (levelUser) {
                    LEVEL_CUSTOMER -> {
                        viewModel.insertCustomer(
                            Customer(
                                etName.text.toString(),
                                email,
                                etPhone.text.toString(),
                                etAddress.text.toString(),
                                etJob.text.toString(),
                                levelUser,
                                userLocation.latitude,
                                userLocation.longitude
                            )
                        ).observe(requireActivity()) {
                            if (it == 1) {
                                navController.navigate(R.id.action_registerFragment_to_customerLoginFragment)

                            }
                        }
                    }
                    LEVEL_ADMIN -> {
                        viewModel.insertAdmin(
                            Admin(
                                etName.text.toString(),
                                email,
                                etPhone.text.toString(),
                                etAddress.text.toString(),
                                etJob.text.toString(),
                                levelUser,
                            )
                        ).observe(requireActivity()) {
                            if (it == 1) {
                                navController.navigate(R.id.action_registerFragment_to_customerLoginFragment)
                            }
                        }

                    }
                }

            } else {
                Log.d(TAG, ": actionRegister failed")

            }
        })

    }

    private fun updateUI() {
        when (levelUser) {
            LEVEL_CUSTOMER -> {
                llLatLng.visibility = View.VISIBLE
            }
            LEVEL_ADMIN -> {
                llLatLng.visibility = View.GONE

            }
        }
        if (::userLocation.isInitialized) {
            etLatLng.setText(userLocation.toString())
        }
    }

    private fun loading(showLoading: Boolean) {
        if (showLoading) {
            SpinKitUtils.show(spinKitLoading)
        } else {
            SpinKitUtils.hide(spinKitLoading)
        }
    }
}