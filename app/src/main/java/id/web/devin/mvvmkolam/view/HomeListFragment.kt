package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentHomeListBinding
import id.web.devin.mvvmkolam.viewmodel.ListViewModel

class HomeListFragment : Fragment() {
    private lateinit var viewModel:ListViewModel
    private val kolamListAdapter = KolamListAdapter(arrayListOf())
    private lateinit var b: FragmentHomeListBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b= FragmentHomeListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        b.recViewKolam.layoutManager = LinearLayoutManager(context)
        b.recViewKolam.adapter = kolamListAdapter

        b.refreshLayout.setOnRefreshListener {
            b.recViewKolam.visibility = View.GONE
            b.txtError.visibility = View.GONE
            b.progressLoad.visibility = View.VISIBLE
            viewModel.refresh()
            b.refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer{
            kolamListAdapter.updateKolamList(it)
        })

        viewModel.loadingErrorLD.observe(viewLifecycleOwner, Observer {
            b.txtError.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressLoad.visibility = View.VISIBLE
                b.recViewKolam.visibility = View.GONE
            }else{
                b.progressLoad.visibility = View.GONE
                b.recViewKolam.visibility = View.VISIBLE
            }
        })
    }
}