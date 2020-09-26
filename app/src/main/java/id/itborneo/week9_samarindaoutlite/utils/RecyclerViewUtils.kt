package id.itborneo.week9_samarindaoutlite.utils

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtils {
    //attach on swipe to delete
    fun attachOnSwipe(
        context: Context,
        recyclerView: RecyclerView,
        onSwipeCalled: (RecyclerView.ViewHolder) -> Unit
    ) {
        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                onSwipeCalled(viewHolder)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}