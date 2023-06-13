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
import id.web.devin.mvvmkolam.databinding.FragmentPelatihListBinding
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel

class PelatihListFragment : Fragment() {
    private lateinit var b:FragmentPelatihListBinding
    private lateinit var viewModel: DetailKolamViewModel
    private val pelatihListAdapter = PelatihListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPelatihListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val kolamID = GlobalData.kolamID
        Log.d("kolamPelatih",kolamID)
        viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        viewModel.fetchData(kolamID)

        b.recViewPelatih.layoutManager = LinearLayoutManager(context)
        b.recViewPelatih.adapter = pelatihListAdapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            pelatihListAdapter.updatePelatihList(it.pelatih)
        })
    }
}