package id.itborneo.week9_samarindaoutlite.ui.home.outlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import kotlinx.android.synthetic.main.item_outlite.view.*

class OutlineAdapter(
    private val clickListener: (OutliteModel) -> Unit
) : RecyclerView.Adapter<OutlineAdapter.ViewHolder>() {


    private var items = listOf<OutliteModel>()
    fun setOutlites(items: List<OutliteModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outlite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(OutliteModel: OutliteModel) {
            itemView.tvLokasi.text = OutliteModel.location
            itemView.tvName.text = OutliteModel.name


            itemView.setOnClickListener {
                clickListener(OutliteModel)
            }
        }
    }
}