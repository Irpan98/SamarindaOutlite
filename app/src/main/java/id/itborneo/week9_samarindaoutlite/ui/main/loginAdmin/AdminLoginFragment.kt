package id.itborneo.week9_samarindaoutlite.ui.main.loginAdmin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.ui.main.MainViewModel
import id.itborneo.week9_samarindaoutlite.utils.*
import kotlinx.android.synthetic.main.fragment_admin_login.*


class AdminLoginFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel
    private val levelUser = LEVEL_ADMIN
    private val TAG = "UserLoginFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initLoginListener()
        initLoginAsAdminListener()
        initRegisterListener()

        viewModel = MainViewModel()

    }


    private fun initLoginListener() {
        btnLogin.setOnClickListener {
            if (isInputEmpty()) return@setOnClickListener

//            loading(true)

            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            viewModel.login(email, pass).observe(requireActivity()) { task ->
                loading(false)

                if (task.isSuccessful) {

                    actionLogin()

                } else {
                    Log.d(TAG, ": actionLogin failed")

                    Toast.makeText(requireContext(), "Email / Password Salah", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }

    private fun isInputEmpty(): Boolean {

        val isEmailNull = ViewUtils.validateInput(etEmail, "Email tidak boleh kosong")
        val isPassNull = ViewUtils.validateInput(etPassword, "Password tidak boleh kosong")

        return isEmailNull ||
                isPassNull

    }

    private fun loading(showLoading: Boolean) {
        if (showLoading) {
            SpinKitUtils.show(spinKitLoading)
        } else {
            SpinKitUtils.hide(spinKitLoading)
        }
    }


    private fun actionLogin() {
        Log.d(TAG, ": actionLogin isSuccessful")
        if (levelUser == LEVEL_ADMIN) {
            viewModel.getUserAdmin().observe(requireActivity()) {
                Toast.makeText(requireContext(), "Login As User", Toast.LENGTH_SHORT).show()

                Log.d(TAG, ": actionLogin getUserAdmin: ${it.name}")
                SharedPrefUtils(requireActivity()).putBooleanData(true)

                if (it != null) {
                    actionMoveToHome(it)
                }
            }
        }


    }

    private fun actionMoveToHome(admin: Admin) {
        val bundle = bundleOf(
            EXTRA_ADMIN to admin,
        )
        navController.navigate(R.id.action_adminLoginFragment_to_homeActivity, bundle)

    }


    private fun initRegisterListener() {
        tvRegister.setOnClickListener {
            navController.navigate(R.id.action_adminLoginFragment_to_registerFragment)
        }
    }

    private fun initLoginAsAdminListener() {
        tvLoginAsCustomer.setOnClickListener {
            navController.navigate(R.id.action_adminLoginFragment_to_customerLoginFragment)
        }
    }

}