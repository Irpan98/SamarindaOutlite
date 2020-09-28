package id.itborneo.week9_samarindaoutlite.ui.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.user.Admin
import id.itborneo.week9_samarindaoutlite.data.model.user.Customer
import id.itborneo.week9_samarindaoutlite.utils.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        getDataAndSendToFragment()
        initViewModel()
        initGetDataIntent()
        attachBottomNav()
    }

    private fun getDataAndSendToFragment() {

        findNavController(R.id.homeNavHostFragment)
            .setGraph(R.navigation.home_nav_graph, intent.extras)

        Log.d(TAG, "intentUser" + intent?.getParcelableExtra<Customer>(EXTRA_USER).toString())

    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[HomeViewModel::class.java]
    }


    private fun initGetDataIntent() {
        val customerIntent = intent?.getParcelableExtra<Customer>(EXTRA_CUSTOMER)
        val adminIntent = intent?.getParcelableExtra<Admin>(EXTRA_ADMIN)
        Log.d(TAG, "intentUser" + intent.getParcelableExtra<Customer>(EXTRA_USER).toString())
        if (customerIntent != null) {
            viewModel.userLevel = LEVEL_CUSTOMER
            viewModel.customer = customerIntent
        } else if (adminIntent != null) {
            viewModel.userLevel = LEVEL_ADMIN

            viewModel.admin = adminIntent
        }

    }

    private fun attachBottomNav() {

        when (viewModel.userLevel) {
            LEVEL_ADMIN -> {
                bottomNavAdmin()

            }
            LEVEL_CUSTOMER -> {
                bottomNavCustomer()

            }
        }
    }

    private fun bottomNavAdmin() {
        val navController = Navigation.findNavController(this, R.id.homeNavHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    private fun bottomNavCustomer() {
        bottomNavigationView.menu.clear() //clear old inflated items.
        bottomNavigationView.inflateMenu(R.menu.home_customer_bottom_nav)
        val navController = Navigation.findNavController(this, R.id.homeNavHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }


}