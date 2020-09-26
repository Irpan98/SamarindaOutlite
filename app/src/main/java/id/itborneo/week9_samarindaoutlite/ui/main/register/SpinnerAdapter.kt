package id.itborneo.week9_samarindaoutlite.ui.main.register

import android.view.View
import android.widget.AdapterView
import id.itborneo.week9_samarindaoutlite.utils.LEVEL_ADMIN
import id.itborneo.week9_samarindaoutlite.utils.LEVEL_CUSTOMER

class SpinnerListener(private val levels: List<String>, private val listener: (String) -> Unit) :
    AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener {


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        listener(levels[position])
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        listener(levels[0])
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }


}