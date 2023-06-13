package id.web.devin.mvvmkolam.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentTiketListBinding
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel

class TiketListFragment : Fragment() {
    private lateinit var b:FragmentTiketListBinding
    private lateinit var viewModel:DetailKolamViewModel
    private val tiketListAdapter = TiketListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentTiketListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(arguments != null){
//            val kolamID = TiketListFragmentArgs.fromBundle(requireArguments()).kolamID
            val kolamID = GlobalData.kolamID
            Log.d("kolamTiket",kolamID)
            viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
            viewModel.fetchData(kolamID)

            b.recViewTiket.layoutManager = LinearLayoutManager(context)
            b.recViewTiket.adapter = tiketListAdapter

//            b.refreshLayoutTiket.setOnRefreshListener {
//                b.recViewTiket.visibility = View.GONE
//                viewModel.fetchData(kolamID)
//                b.refreshLayoutTiket.isRefreshing = false
//            }
            observeViewModel()
//        }
    }

    private fun observeViewModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            tiketListAdapter.updateTiketList(it.tiket)
        })
    }
}