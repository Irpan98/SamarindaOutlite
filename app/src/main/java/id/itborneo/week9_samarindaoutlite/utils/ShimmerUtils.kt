package id.itborneo.week9_samarindaoutlite.utils

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

object ShimmerUtils {

    fun stopShimmer(shimmer: ShimmerFrameLayout) {
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
    }
    fun startShimmer(shimmer: ShimmerFrameLayout) {
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
    }

}