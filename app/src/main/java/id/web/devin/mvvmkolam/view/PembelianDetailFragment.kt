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
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentPembelianDetailBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.model.Transaction
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.util.formatDateTime
import id.web.devin.mvvmkolam.viewmodel.CartViewModel
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel
import id.web.devin.mvvmkolam.viewmodel.ShippingViewModel
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PembelianDetailFragment : Fragment() {
    private lateinit var b: FragmentPembelianDetailBinding
    private lateinit var pembelianDetailItemAdapter: PembelianDetailItemAdapter
    private lateinit var vMPengguna: ProfilViewModel
    private lateinit var viewModel: ShippingViewModel
    private lateinit var vMTransaksi: TransactionViewModel
    lateinit var email:String
    private var idtrx:String = ""
    private var statusPembelian:String = ""
    private var tujuan:String = ""
    private var asal:String = ""
    private var berat:Int = 0
    private var pengiriman:Int = 0
    private var totalBerat:Int = 0
    private var alamat: String = ""
    private var role:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPembelianDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        role = Global.getRole(requireContext()).toString()
        val sharedPreferences = requireActivity().getSharedPreferences("idtransaksi", Context.MODE_PRIVATE)
        val status = sharedPreferences.getString("status", null)
        val id = sharedPreferences.getString("idtrx", null)
        idtrx = id.toString()
        statusPembelian = status.toString()
        vMPengguna = ViewModelProvider(this).get(ProfilViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ShippingViewModel::class.java)
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)

        vMPengguna.fetchProfil(email)
        if(role == Role.Admin.name){
            vMTransaksi.fetchTransaksiAdminDetail(email, statusPembelian, idtrx)
            b.btnKonfirmasiPesanan.visibility = View.VISIBLE
            b.btnBayarSekarang.visibility = View.GONE
            b.txtNoResi.visibility = View.GONE
            b.textViewResi.visibility = View.GONE
            b.btnPesananDiterima.visibility = View.GONE
            b.btnBelanjaLagi.visibility = View.GONE
            b.txtLihatBukti.setOnClickListener {
                val action = PembelianDetailFragmentDirections.actionBuktiPembayaranFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }else{
            vMTransaksi.fetchTransactionDetail(email, statusPembelian, idtrx)
            b.btnKonfirmasiPesanan.visibility = View.GONE
            b.btnBayarSekarang.visibility = View.VISIBLE
            b.txtLihatBukti.visibility = View.GONE
            b.txtResi.visibility = View.GONE
            b.btnBelanjaLagi.visibility = View.VISIBLE
            b.btnPesananDiterima.visibility = View.VISIBLE
            b.btnBayarSekarang.setOnClickListener {
                val action = PembelianDetailFragmentDirections.actionPembayaranFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }

        b.btnBatalkanPesanan.setOnClickListener {
            vMTransaksi.updateStatus(idtrx,StatusTransaksi.Dibatalkan.name)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        b.btnPesananDiterima.setOnClickListener {
            vMTransaksi.updateStatus(idtrx,StatusTransaksi.Diterima.name)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        b.btnBelanjaLagi.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        pembelianDetailItemAdapter = PembelianDetailItemAdapter(arrayListOf())
        b.recViewPembelianDetail.layoutManager = LinearLayoutManager(context)
        b.recViewPembelianDetail.adapter = pembelianDetailItemAdapter
        observeView()
    }

    private fun observeView() {
        viewModel.shippingCosts.observe(viewLifecycleOwner, Observer {
            it.rajaongkir.results.forEach {
                var ongkir =  it.costs[0].cost[0].value
                val jasaKirim = it.name
                val layanan = it.costs[0].service
                pengiriman = ongkir
                b.txtJasaPengiriman.text = "$jasaKirim ($layanan)"
                b.txtBiayaOngkir.text = formatCurrency(ongkir.toDouble())
            }
        })
        vMPengguna.userLD.observe(viewLifecycleOwner, Observer {
            b.txtNamaPembelian.text = "${it.nama} |"
            b.txtAlamatPembelian.text = "${it.alamat}, ${it.kota}"
            b.txtNoTelepon.text = it.telepon
            alamat = it.alamat.toString()
            viewModel.fetchShippingCosts(asal,tujuan,totalBerat)
        })

        vMTransaksi.transaksiDetailLD.observe(viewLifecycleOwner, Observer {transaksi->
            if(transaksi.status.toString() == StatusTransaksi.Dikirim.name || transaksi.status.toString() == StatusTransaksi.Diterima.name || transaksi.status.toString() == StatusTransaksi.Dibatalkan.name){
                b.btnBayarSekarang.visibility = View.GONE
                b.btnKonfirmasiPesanan.visibility = View.GONE
                b.btnBatalkanPesanan.visibility = View.GONE
                b.txtNoResi.visibility = View.VISIBLE
                b.textViewResi.visibility = View.VISIBLE
                b.txtResi.visibility = View.GONE
                b.txtMenunggu.visibility = View.GONE
            }
            if(transaksi.status.toString() == StatusTransaksi.Diproses.name || transaksi.status.toString() == StatusTransaksi.Diterima.name || transaksi.status.toString() == StatusTransaksi.Dibatalkan.name){
                b.btnPesananDiterima.visibility = View.GONE
            }else{
                b.btnPesananDiterima.visibility = View.VISIBLE
            }
            if(transaksi.status.toString() == StatusTransaksi.Diproses.name || transaksi.status.toString() == StatusTransaksi.Dikirim.name){
                b.btnBelanjaLagi.visibility = View.GONE
            }else{
                b.btnBelanjaLagi.visibility = View.VISIBLE
            }
            pembelianDetailItemAdapter.updateTransactionDetailItem(transaksi.produk)
            b.txtNoPesanan.text = transaksi.id

            b.txtKolamPembelianDetail.text = transaksi.namaKolam
            asal = transaksi.idkota
            Log.d("asal", asal)

            transaksi.produk.forEach {produk->
                //update status dan masukan no resi
                b.btnKonfirmasiPesanan.setOnClickListener {
                    if(b.txtInputResi.text.toString() != ""){
                        val resi = b.txtInputResi.text.toString()
                        vMTransaksi.konfirmasiTransaksi(idtrx,StatusTransaksi.Dikirim.name,resi)
                        Toast.makeText(context, "Dikonfirmasi",Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        AlertDialog.Builder(context).apply {
                            val title = SpannableString("Peringatan")
                            title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                            val message = SpannableString("Isi nomor resi sebelum mengkonfirmasi pesanan")
                            message.setSpan(
                                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                0,
                                message.length,
                                0
                            )
                            setTitle(title)
                            setMessage(message)
                            setPositiveButton("OK",null)
                            create().show()
                        }
                    }
                }

                //Cek bukti
                if(produk.urlBukti == ""){
                    b.txtStatusPembayaran.text = "Status : Belum Dibayar"
                    b.txtTanggalPembayaran.visibility = View.GONE
                    b.textViewTanggal.visibility = View.GONE
                }else {
                    b.txtLihatBukti.visibility = View.VISIBLE
                    b.txtLihatBukti.setOnClickListener {
                        val action = PembelianDetailFragmentDirections.actionBuktiPembayaranFragment()
                        Navigation.findNavController(it).navigate(action)
                    }
                    b.txtStatusPembayaran.text = "Status : Sudah dibayar"
                    b.txtMenunggu.text = "Menunggu konfirmasi penjual"
                    b.btnBayarSekarang.visibility = View.GONE
                    b.txtTanggalPembayaran.visibility = View.VISIBLE
                    b.textViewTanggal.visibility = View.VISIBLE
                    b.txtTanggalPembayaran.text = formatDateTime(produk.tanggalBayar)
                }
                //cek resi
                if(produk.no_resi == ""){
                    b.txtNoResi.text = "-"
                }else{
                    b.txtNoResi.text = produk.no_resi
                }

                tujuan = produk.idkota.toString()
                b.txtTotalPesananPembelian.text = formatCurrency(produk.total_harga)
                b.txtWaktuPemesanan.text = formatDateTime(produk.tanggal)
                if(produk.qty > 1){
                    berat = produk.berat.toInt() * produk.qty
                }else{
                    berat = produk.berat.toInt()
                }

                totalBerat += berat


            }

            viewModel.fetchShippingCosts(asal,tujuan,totalBerat)
        })
    }
}