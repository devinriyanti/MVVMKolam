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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentPelatihListBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.Global
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
        b.txtErorPelatih.visibility = View.GONE
        val role = context?.let { Global.getRole(it) }
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        Log.d("das",id.toString());
        viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        viewModel.fetchData(id.toString())

        b.recViewPelatih.layoutManager = LinearLayoutManager(context)
        b.recViewPelatih.adapter = pelatihListAdapter

        if(role == Role.Admin.name){
            b.fabTambahPelatih.visibility = View.VISIBLE

            b.fabTambahPelatih.setOnClickListener{
                val action = KolamDetailFragmentDirections.actionPelatihAddFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }
        b.refreshLayoutPelatih.setOnRefreshListener {
            b.recViewPelatih.visibility = View.GONE
            b.txtErorPelatih.visibility = View.GONE
            b.progressLoadPelatih.visibility = View.VISIBLE
            viewModel.fetchData(id.toString())
            b.refreshLayoutPelatih.isRefreshing = false
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            if(!it.pelatih.isNullOrEmpty()){
                pelatihListAdapter.updatePelatihList(it.pelatih)
            }else{
               b.txtPelatihAvailable.setText("Tidak Ada Pelatih")
            }
        })
        viewModel.loadingErrorLD.observe(viewLifecycleOwner, Observer {
            b.txtErorPelatih.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressLoadPelatih.visibility = View.VISIBLE
                b.recViewPelatih.visibility = View.GONE
            }else{
                b.progressLoadPelatih.visibility = View.GONE
                b.recViewPelatih.visibility = View.VISIBLE
            }
        })
    }
}