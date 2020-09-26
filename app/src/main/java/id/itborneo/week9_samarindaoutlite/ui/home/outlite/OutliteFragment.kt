package id.itborneo.week9_samarindaoutlite.ui.home.outlite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.week9_samarindaoutlite.R
import id.itborneo.week9_samarindaoutlite.data.OutliteRepository
import id.itborneo.week9_samarindaoutlite.data.model.OutliteModel
import id.itborneo.week9_samarindaoutlite.ui.home.HomeViewModel
import id.itborneo.week9_samarindaoutlite.ui.home.outliteAddUpdate.AddUpdateOutliteActivity
import id.itborneo.week9_samarindaoutlite.ui.mapMarker.MapMarkerActivity.Companion.REQ_MAP
import id.itborneo.week9_samarindaoutlite.utils.EXTRA_OUTLITE
import id.itborneo.week9_samarindaoutlite.utils.RecyclerViewUtils
import id.itborneo.week9_samarindaoutlite.utils.ShimmerUtils
import kotlinx.android.synthetic.main.fragment_outlite.*
import kotlinx.android.synthetic.main.partial_shimmer.*


class OutliteFragment : Fragment() {

    private val TAG = "OutliteFragment"
    private lateinit var adapter: OutlineAdapter
    private var outlites = mutableListOf<OutliteModel>()
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outlite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getOutlites()
        attachButtonListener()
        attachRecyclerView()
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[HomeViewModel::class.java]
        } ?: throw Exception("Salah Activity")
    }

    private fun getOutlites() {
        loading(true)
        OutliteRepository().getAllOutlite().observe(requireActivity(), {
            this.loading(false)

            outlites = it.toMutableList()
            adapter.setOutlites(it)
        })
    }

    private fun attachButtonListener() {
        fabAddUpdate.setOnClickListener {
            actionToAddUpdateOutileActivity()
        }
    }


    private fun actionToAddUpdateOutileActivity(outlite: OutliteModel? = null) {
        Log.d(TAG, "ActionMoveActivity $outlite")
        val intent = Intent(context, AddUpdateOutliteActivity::class.java)
        if (outlite != null) {
            intent.putExtra(EXTRA_OUTLITE, outlite)

        }
        startActivityForResult(intent, REQ_MAP)
    }


    private fun attachRecyclerView() {
        rvOutlites.layoutManager = LinearLayoutManager(requireContext())
        adapter = OutlineAdapter {

            actionToAddUpdateOutileActivity(it)
        }
        rvOutlites.adapter = adapter
        recyclerViewItemDelete()
    }

    private fun recyclerViewItemDelete() {
        RecyclerViewUtils.attachOnSwipe(requireContext(), rvOutlites) {

//            ViewsUtils.setDialogComfirm(requireContext(), {
//                Log.d(TAG, "setupOnSwipe + delete ${it.adapterPosition}")
            //delete
            val items = outlites[it.adapterPosition]
            viewModel.deleteOutlite(items.id).observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Berhasil Menghapus Item", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun loading(showLoading: Boolean) {
        if (showLoading) {
            ShimmerUtils.startShimmer(sfLoading)
        } else {
            ShimmerUtils.stopShimmer(sfLoading)
        }
    }

}


