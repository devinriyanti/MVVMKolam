package id.web.devin.mvvmkolam.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentBuktiPembayaranBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class BuktiPembayaranFragment : Fragment() {
    private lateinit var b:FragmentBuktiPembayaranBinding
    private lateinit var vMTransaksi: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentBuktiPembayaranBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = context?.let { Global.getEmail(it) }
        val sharedPreferences = requireActivity().getSharedPreferences("idtransaksi", Context.MODE_PRIVATE)
        val status = sharedPreferences.getString("status", null)
        val id = sharedPreferences.getString("idtrx", null)
        val role = context?.let { Global.getRole(it) }
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)
        if(role == Role.Admin.name){
            vMTransaksi.fetchTransaksiAdminDetail(email.toString(), status.toString(),id.toString())
        }else{
            vMTransaksi.fetchTransactionDetail(email.toString(), status.toString(),id.toString())
        }
        b.btnKembali.setOnClickListener {
            val action = BuktiPembayaranFragmentDirections.actionPembelianDetailFragment()
            Navigation.findNavController(it).navigate(action)
        }
        observeView()
    }

    private fun observeView() {
        vMTransaksi.transaksiDetailLD.observe(viewLifecycleOwner, Observer {
            it.produk.forEach {
                b.imgBukti.loadImage(it.urlBukti, b.progressBarBukti)
            }
        })
    }

}