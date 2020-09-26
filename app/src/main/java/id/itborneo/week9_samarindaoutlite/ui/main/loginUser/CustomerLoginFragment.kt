package id.itborneo.week9_samarindaoutlite.ui.main.loginUser

import android.content.Intent
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
import com.google.firebase.auth.FirebaseUser
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.data.model.user.Customer
import id.itborneo.week9_samarindaoutlite.ui.main.MainViewModel
import id.itborneo.week9_samarindaoutlite.utils.*
import kotlinx.android.synthetic.main.fragment_customer_login.*

class CustomerLoginFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel
    private lateinit var currentUser: FirebaseUser
    private val levelUser = LEVEL_CUSTOMER
    private val TAG = "CustomerLoginFragment"
    private lateinit var sharedPref: SharedPrefUtils
    private lateinit var googleLogin: CustomerGoogleLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initRegisterListener()
        initLoginAsAdminListener()
        initLoginListener()
        initAuthGoogleListener()
        initAuthGmail()
        viewModel = MainViewModel()

    }


    private fun initAuthGoogleListener() {
        googleLogin = CustomerGoogleLogin(this)
        googleLogin.initAuthGmail()
        btnSignInGoogle.setOnClickListener {
            loading(true)

            googleLogin.signIn()
        }
    }

    private fun initAuthGmail() {

    }

    private fun isInputEmpty(): Boolean {

        val isEmailNull = ViewUtils.validateInput(etEmail, "Email tidak boleh kosong")
        val isPassNull = ViewUtils.validateInput(etPassword, "Password tidak boleh kosong")

        return isEmailNull ||
                isPassNull

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult:" + "onActivityResult")
        loading(false)

        googleLogin.onActivityResult(requestCode, resultCode, data) {
            val customer = Customer(
                it.displayName ?: "",
                it.email ?: "",
            )
            actionMoveToHome(customer)
        }
    }

    private fun loading(showLoading: Boolean) {
        if (showLoading) {
            SpinKitUtils.show(spinKitLoading)
        } else {
            SpinKitUtils.hide(spinKitLoading)
        }
    }

    private fun initLoginListener() {
        btnLogin.setOnClickListener {
            if (isInputEmpty()) return@setOnClickListener
            loading(true)
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            viewModel.login(email, pass).observe(requireActivity()) { task ->

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


    private fun actionLogin() {
        loading(true)

        Log.d(TAG, ": actionLogin isSuccessful")
        if (levelUser == LEVEL_CUSTOMER) {
            viewModel.getUserCustomer().observe(requireActivity()) {
                Log.d(TAG, ": actionLogin getUserCustomer: $it")
                sharedPref.putBooleanData(true)
                if (it != null) {
                    loading(false)

                    Toast.makeText(requireContext(), "Login As User", Toast.LENGTH_SHORT).show()

                    actionMoveToHome(it)

                } else {
                    viewModel.getUserAdmin().observe(requireActivity()) { admin ->
                        Log.d(TAG, ": actionLogin getUserAdmin: $admin")
                        if (admin != null) {
                            Toast.makeText(requireContext(), "Login As Admin", Toast.LENGTH_SHORT)
                                .show()
                            actionMoveToHome(admin)
                        }
                    }
                }
            }
        }
    }

    private fun actionMoveToHome(customer: Customer) {
        val bundle = bundleOf(
            EXTRA_CUSTOMER to customer,
        )
        navController.navigate(R.id.action_customerLoginFragment_to_homeActivity, bundle)

    }

    private fun actionMoveToHome(admin: Admin) {
        val bundle = bundleOf(
            EXTRA_ADMIN to admin,

            )
        navController.navigate(R.id.action_customerLoginFragment_to_homeActivity, bundle)

    }

    private fun initRegisterListener() {
        tvRegister.setOnClickListener {
            navController.navigate(R.id.action_customerLoginFragment_to_registerFragment)
        }
    }

    private fun initLoginAsAdminListener() {
        tvLoginAsAdmin.setOnClickListener {
            navController.navigate(R.id.action_customerLoginFragment_to_adminLoginFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        getLastLogin()
    }

    private fun getLastLogin() {

        sharedPref = SharedPrefUtils(requireActivity())
        if (!sharedPref.getBool()) return

        val currentUser = viewModel.firebaseAuth().currentUser
        if (currentUser != null) {

            this.currentUser = currentUser
            actionLogin()
        } else {
            sharedPref.removeBool()
        }
    }
}