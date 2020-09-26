package id.itborneo.week9_samarindaoutlite.ui.home.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.ui.home.HomeViewModel
import id.itborneo.week9_samarindaoutlite.ui.main.MainActivity
import id.itborneo.week9_samarindaoutlite.utils.LEVEL_ADMIN
import id.itborneo.week9_samarindaoutlite.utils.LEVEL_CUSTOMER
import id.itborneo.week9_samarindaoutlite.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
//        initGetDataIntent()
        updateUI()
        attachLogoutBtn()

    }


    private fun updateUI() {

        when (viewModel.userLevel) {
            LEVEL_CUSTOMER -> {

                tvUser.text = viewModel.customer.name
                tvWelcomeUser.text = viewModel.customer.name
                tvEmail.text = viewModel.customer.email

            }
            LEVEL_ADMIN -> {

                tvUser.text = viewModel.admin.name
                tvWelcomeUser.text = viewModel.admin.name
                tvEmail.text = viewModel.admin.email
            }
            else -> {
                return
            }
        }
    }


    private fun attachLogoutBtn() {
        btnLogOut.setOnClickListener {
            viewModel.logoutUser().observe(requireActivity()) {
                if (it == 1) {

                    activity?.onBackPressed()
                }
            }
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


}