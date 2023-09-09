package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentCheckoutBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.viewmodel.CartViewModel
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel
import id.web.devin.mvvmkolam.viewmodel.ShippingViewModel
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class CheckoutFragment : Fragment() {
    private lateinit var b:FragmentCheckoutBinding
    private lateinit var vMShipping: ShippingViewModel
    private lateinit var vMPengguna: ProfilViewModel
    private lateinit var vMCart: CartViewModel
    private lateinit var vMTransaksi: TransactionViewModel
    private lateinit var checkoutItemAdapter: CheckoutItemAdapter
    lateinit var email:String
    private var idKolam:String = ""
    private var tujuan:String = ""
    private var asal:String = ""
    private var berat:Int = 0
    private var subtotal:Int = 0
    private var pengiriman:Int = 0
    private var totalBerat:Int = 0
    private var idkeranjang: Int = 0
    private var alamat: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentCheckoutBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        val sharedPreferences = requireActivity().getSharedPreferences("idkolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        if (id != null) {
            idKolam = id
        }

        vMShipping = ViewModelProvider(this).get(ShippingViewModel::class.java)
        vMPengguna = ViewModelProvider(this).get(ProfilViewModel::class.java)
        vMCart = ViewModelProvider(this).get(CartViewModel::class.java)
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)

        vMPengguna.fetchProfil(email)
        vMCart.fetchCartDetail(email, idKolam)

        checkoutItemAdapter = CheckoutItemAdapter(arrayListOf())
        b.recViewProdukCO.layoutManager = LinearLayoutManager(context)
        b.recViewProdukCO.adapter = checkoutItemAdapter

        observeView()
    }

    private fun observeView() {
        vMShipping.shippingCosts.observe(viewLifecycleOwner, Observer {
            it.rajaongkir.results.forEach {
                var ongkir =  it.costs[0].cost[0].value
                val jasaKirim = it.name
                val layanan = it.costs[0].service
                val estimasi = it.costs[0].cost[0].etd
                pengiriman = ongkir
                b.txtJasaKirim.text = jasaKirim
                b.txtLayananPengiriman.text = layanan
                b.txtEstimasiPengiriman.text = "$estimasi Hari"
                b.txtTotalPengirimanCO.text = formatCurrency(ongkir.toDouble())

                updateTotal()
            }
        })

        vMPengguna.userLD.observe(viewLifecycleOwner, Observer {
            b.txtNamaCO.text = "${it.nama} |"
            b.txtAlamatCO.text = "${it.alamat}, ${it.kota}"
            b.txtTeleponCO.text = it.telepon
            tujuan = it.idkota
            alamat = it.alamat.toString()
            Log.d("tujuan", it.idkota)
            updateTotal()
            vMShipping.fetchShippingCosts(asal,tujuan,totalBerat)
        })

        vMCart.cartDetailLD.observe(viewLifecycleOwner, Observer {cart->
            checkoutItemAdapter.updateCheckoutItem(cart.produk)
            Log.d("cart", cart.produk.toString())
            b.txtNamaKolamCO.text = cart.nama
            asal = cart.idkota

            cart.produk.forEach {produk->
                b.txtSubtotalCO.text = formatCurrency(produk.total_harga)
                b.txtTotalPesananCO.text = formatCurrency(produk.total_harga)
                subtotal = produk.total_harga.toInt()
                idkeranjang = produk.idkeranjangs
                if(produk.qty > 1){
                    berat = produk.berat.toInt() * produk.qty
                }else{
                    berat = produk.berat.toInt()
                }

                totalBerat += berat
                Log.d("asal", cart.idkota)
                updateTotal()
            }
            vMShipping.fetchShippingCosts(asal,tujuan,totalBerat)
        })

        vMShipping.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true){
                b.progressCheckout.visibility = View.VISIBLE
                b.cardAlamatCO.visibility = View.GONE
                b.cardProdukCO.visibility = View.GONE
                b.cardTotalPesanan.visibility = View.GONE
                b.cardPengiriman.visibility = View.GONE
                b.cardPembayaran.visibility = View.GONE
            }else{
                b.progressCheckout.visibility = View.GONE
                b.cardAlamatCO.visibility = View.VISIBLE
                b.cardProdukCO.visibility = View.VISIBLE
                b.cardTotalPesanan.visibility = View.VISIBLE
                b.cardPengiriman.visibility = View.VISIBLE
                b.cardPembayaran.visibility = View.VISIBLE
            }
        })
        vMTransaksi.statusLD.observe(viewLifecycleOwner, Observer {
            if(it == false){
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Pesanan Berhasil Dibuat")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK") { _, _ ->
                        val action = CheckoutFragmentDirections.actionToPembayaranFragment()
                        findNavController().navigate(action)
                    }
                    create().show()
                }
            }
        })
    }

    private fun updateTotal() {
        val total = subtotal + pengiriman
        val sharedPreferences = context?.getSharedPreferences("totalPembelian", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor!!.putString("total", total.toString())
        editor!!.apply()
        b.txtTotalPembayaranCO.text = formatCurrency(total.toDouble())
        b.btnBuatPesananCO.setOnClickListener {
            vMTransaksi.insertTransaksi(pengiriman, idkeranjang, idKolam, email, tujuan.toInt(), alamat)
        }
    }
}