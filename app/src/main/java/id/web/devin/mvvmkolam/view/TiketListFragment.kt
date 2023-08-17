package id.web.devin.mvvmkolam.view

import android.content.Context
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
        b.txtErorTiket.visibility = View.GONE
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        viewModel.fetchData(id.toString())

        b.recViewTiket.layoutManager = LinearLayoutManager(context)
        b.recViewTiket.adapter = tiketListAdapter

        b.refreshLayoutTiket.setOnRefreshListener {
            b.recViewTiket.visibility = View.GONE
            b.txtErorTiket.visibility = View.GONE
            b.progressLoadTiket.visibility = View.VISIBLE
            viewModel.fetchData(id.toString())
            b.refreshLayoutTiket.isRefreshing = false
        }
        observeViewModel()


    }

    private fun observeViewModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            if(!it.tiket.isNullOrEmpty()){
                Log.d("ada","tiket")
                tiketListAdapter.updateTiketList(it.tiket)
            }else{
                Log.d("kosong","gaada tiket")
                b.txtTiketStok.setText("Tidak Ada Tiket")
            }
        })
        viewModel.loadingErrorLD.observe(viewLifecycleOwner, Observer {
            b.txtErorTiket.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressLoadTiket.visibility = View.VISIBLE
                b.recViewTiket.visibility = View.GONE
            }else{
                b.progressLoadTiket.visibility = View.GONE
                b.recViewTiket.visibility = View.VISIBLE
            }
        })
    }
}