package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentProductDetailBinding
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.ProductListViewModel

class ProductDetailFragment : Fragment() {
    private lateinit var viewModel:ProductListViewModel
    private lateinit var b:FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentProductDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null){
            val produkID = ProductDetailFragmentArgs.fromBundle(requireArguments()).produkID
            viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
            viewModel.refresh(produkID)

            observeModel()
        }
    }

    private fun observeModel() {
        viewModel.produkLD.observe(viewLifecycleOwner, Observer {
            val produk = viewModel.produkLD.value
            produk?.let { it->
                b.txtNamaProductDetail.text = it.nama
                b.txtHargaProductDetail.text = "Rp. ${it.harga.toString()}"
                b.txtDeskripsi.text = it.deskripsi
                b.imageProductDetail.loadImage(it.gambarUrl.toString(),b.progressBarDetailProduk)
            }
        })
    }
}