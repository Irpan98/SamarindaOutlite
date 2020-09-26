package id.itborneo.week9_samarindaoutlite.utils

import android.view.View
import com.github.ybq.android.spinkit.SpinKitView

object SpinKitUtils {

    fun show(spinKitView: SpinKitView) {
        spinKitView.visibility = View.VISIBLE
    }

    fun hide(spinKitView: SpinKitView) {
        spinKitView.visibility = View.GONE
    }

}