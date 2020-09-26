package id.itborneo.week9_samarindaoutlite.ui.main.splashScreen

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import id.itborneo.week9_samarindaoutlite.R
import kotlinx.android.synthetic.main.fragment_splash_screen.*


@Suppress("DEPRECATION")
class SplashScreenFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initTimerToNavigation()

    }

    private fun initTimerToNavigation() {
        val handler = Handler()
        handler.postDelayed({
            spinKitLoading.visibility = View.GONE
            navController.navigate(R.id.action_splashScreenFragment_to_customerLoginFragment)
        }, 2000)
    }


}