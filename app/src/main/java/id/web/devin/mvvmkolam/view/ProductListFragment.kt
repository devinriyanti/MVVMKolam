package id.web.devin.mvvmkolam.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentProductListBinding
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel
import id.web.devin.mvvmkolam.viewmodel.ProductListViewModel

class ProductListFragment : Fragment() {
    private lateinit var viewModel: DetailKolamViewModel
    private val productListAdapter = ProductListAdapter(arrayListOf())
    private lateinit var b: FragmentProductListBinding
//    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentProductListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(arguments != null){
//            val kolamID = ProductListFragmentArgs.fromBundle(requireArguments()).kolamID
        val kolamID = GlobalData.kolamID
        Log.d("kolamProduk", kolamID)
        viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        viewModel.fetchData(kolamID)

        b.recViewProduct.layoutManager = GridLayoutManager(context, 2)
        b.recViewProduct.adapter = productListAdapter

//        b.refreshLayoutProduct.setOnRefreshListener {
//            b.recViewProduct.visibility = View.GONE
//            viewModel.fetchData(kolamID)
//            b.refreshLayoutProduct.isRefreshing = false
//        }
        observeViewModel()
//    }
    }

    private fun observeViewModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer{
            productListAdapter.updateProductList(it.produk)
        })
    }
}