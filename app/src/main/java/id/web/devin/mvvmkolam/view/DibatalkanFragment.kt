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
import id.web.devin.mvvmkolam.databinding.FragmentDibatalkanBinding
import id.web.devin.mvvmkolam.databinding.FragmentDiprosesBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class DibatalkanFragment : Fragment() {
    private lateinit var b: FragmentDibatalkanBinding
    private lateinit var pembelianListAdapter: PembelianListAdapter
    private lateinit var vMTransaksi: TransactionViewModel
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDibatalkanBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        pembelianListAdapter = PembelianListAdapter(arrayListOf())
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)
        val role = Global.getRole(requireContext())
        if(role == Role.Admin.toString()){
            vMTransaksi.fetchTransaksiAdmin(email,StatusTransaksi.Dibatalkan.name)
        }else{
            vMTransaksi.fetchTransaksiList(email,StatusTransaksi.Dibatalkan.name)
        }
        observeView()
    }

    private fun observeView() {
        vMTransaksi.transaksiLD.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                pembelianListAdapter.updateTransactionList(it)
                it.forEach {
                    if(!it.id.isNullOrEmpty()){
                        b.recViewDibatalkan.layoutManager = LinearLayoutManager(context)
                        b.recViewDibatalkan.adapter = pembelianListAdapter
                    }else{
                        b.txtStatusDibatalkan.text = "Tidak Ada Transaksi"
                    }
                }
            }else{
                b.txtStatusDibatalkan.text = "Tidak Ada Transaksi"
            }
        })
        vMTransaksi.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressBarDIbatalkan.visibility = View.VISIBLE
                b.recViewDibatalkan.visibility = View.GONE
                b.txtStatusDibatalkan.visibility = View.GONE
            }else{
                b.progressBarDIbatalkan.visibility = View.GONE
                b.recViewDibatalkan.visibility = View.VISIBLE
                b.txtStatusDibatalkan.visibility = View.VISIBLE
            }
        })
    }

}