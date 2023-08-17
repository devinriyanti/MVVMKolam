package id.web.devin.mvvmkolam.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentProductDetailBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.CartViewModel
import id.web.devin.mvvmkolam.viewmodel.ProductListViewModel

class ProductDetailFragment : Fragment() {
    private lateinit var viewModel:ProductListViewModel
    private lateinit var b:FragmentProductDetailBinding
    private var idKolam:String? = null
    private var qty:Int? = 0
    private var total:Double? = null
    private val cartViewModel: CartViewModel by viewModels()
    private var email:String? = null
    private var role:String? = null

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
        email = context?.let { Global.getEmail(it) }.toString()
        role = context?.let { Global.getRole(it) }.toString()
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
                val harga = it.harga?.let { it -> formatCurrency(it) }
                val diskon = it.diskon
                b.txtHargaProductDetail.text = harga
                b.txtDeskripsi.text = it.deskripsi
                b.txtBeratProduk.text = it.berat.toString()
                if(!diskon!!.equals(0.0)){
                    b.txtDiskonProduk.text = "Diskon $diskon%"
                }else{
                    b.txtDiskonProduk.visibility = View.GONE
                }
                b.imageProductDetail.loadImage(it.gambarUrl.toString(),b.progressBarDetailProduk)

                val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
                idKolam = sharedPreferences.getString("id", null)
                val harga2 = it.harga
                val idproduk = it.idproduk
                //jangan lupa cek qty di produk ada atau tidak
                //...code here
                if(role == Role.Admin.toString()){
                    b.btnTambahKeranjang.visibility = View.GONE
                    b.btnEditProdukDetail.visibility = View.VISIBLE
                    b.btnHapusProdukDetail.visibility = View.VISIBLE
                }else{
                    b.btnTambahKeranjang.visibility = View.VISIBLE
                    b.btnTambahKeranjang.setOnClickListener {
                        qty = qty!! + 1 // salah, contoh aja
                        total = qty!! * (harga2!!-(harga2 * diskon!!/100))
                        cartViewModel.addToCart(idKolam!!, total!!,email!!, idproduk!!, qty!!, harga2, diskon)
                        Toast.makeText(context,"Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    }
                    b.btnEditProdukDetail.visibility = View.GONE
                    b.btnHapusProdukDetail.visibility = View.GONE
                }
            }
        })
    }
}