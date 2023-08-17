package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.databinding.FragmentHomeListBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.KolamListViewModel
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel

class HomeListFragment : Fragment() {
    private lateinit var viewModel:KolamListViewModel
    private lateinit var vM:ProfilViewModel
    private lateinit var kolamListAdapter: KolamListAdapter
    private lateinit var b: FragmentHomeListBinding
    lateinit var email:String

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
        email = context?.let { Global.getEmail(it) }.toString()
        b.txtError.visibility = View.GONE
        viewModel = ViewModelProvider(this).get(KolamListViewModel::class.java)
        vM = ViewModelProvider(this).get(ProfilViewModel::class.java)
        vM.fetchProfil(email)

        observeViewModel()
    }

    fun observeViewModel(){
        vM.userLD.observe(viewLifecycleOwner, Observer {
            if (it.role == Role.Admin){
                viewModel.refreshAdmin(email,it.role.toString())
                b.fabTambahKolam.visibility = View.VISIBLE

                b.fabTambahKolam.setOnClickListener {
                    val action = HomeListFragmentDirections.actionToKolamAddFragment()
                    Navigation.findNavController(it).navigate(action)
                }
            }else{
                viewModel.refresh()
                b.fabTambahKolam.visibility = View.GONE
            }

            kolamListAdapter = KolamListAdapter(requireContext(),arrayListOf())
            b.recViewKolam.layoutManager = LinearLayoutManager(context)
            b.recViewKolam.adapter = kolamListAdapter

            b.refreshLayout.setOnRefreshListener {
                b.recViewKolam.visibility = View.GONE
                b.txtError.visibility = View.GONE
                b.txtKolamTersedia.visibility = View.GONE
                b.progressLoad.visibility = View.VISIBLE
                if (it.role == Role.Admin){
                    viewModel.refreshAdmin(email,it.role.toString())
                }else{
                    viewModel.refresh()
                }
                b.refreshLayout.isRefreshing = false
            }
        })

        viewModel.kolamLD.observe(viewLifecycleOwner, Observer{
            if(!it.isNullOrEmpty()){
                kolamListAdapter.updateKolamList(it)
                b.txtKolamTersedia.text = ""
            }else{
               b.txtKolamTersedia.text = "Tidak Ada Kolam"
            }
        })

        viewModel.loadingErrorLD.observe(viewLifecycleOwner, Observer {
            b.txtError.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressLoad.visibility = View.VISIBLE
                b.recViewKolam.visibility = View.GONE
                b.txtKolamTersedia.visibility = View.GONE
            }else{
                b.progressLoad.visibility = View.GONE
                b.recViewKolam.visibility = View.VISIBLE
                b.txtKolamTersedia.visibility = View.VISIBLE
            }
        })
    }
}