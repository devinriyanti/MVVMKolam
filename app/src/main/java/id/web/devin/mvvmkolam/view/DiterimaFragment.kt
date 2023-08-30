package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentDikirimBinding
import id.web.devin.mvvmkolam.databinding.FragmentDiterimaBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class DiterimaFragment : Fragment() {
    private lateinit var b: FragmentDiterimaBinding
    private lateinit var pembelianListAdapter: PembelianListAdapter
    private lateinit var vMTransaksi: TransactionViewModel
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDiterimaBinding.inflate(layoutInflater)
        return b.root
        return inflater.inflate(R.layout.fragment_diterima, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        pembelianListAdapter = PembelianListAdapter(arrayListOf())
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)
        val role = Global.getRole(requireContext())
        if(role == Role.Admin.name){
            vMTransaksi.fetchTransaksiAdmin(email, StatusTransaksi.Diterima.name)
        }else{
            vMTransaksi.fetchTransaksiList(email, StatusTransaksi.Diterima.name)
        }
        observeView()
    }

    private fun observeView() {
        vMTransaksi.transaksiLD.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                pembelianListAdapter.updateTransactionList(it)
                it.forEach {
                    if(!it.id.isNullOrEmpty()){
                        b.recViewDiterima.layoutManager = LinearLayoutManager(context)
                        b.recViewDiterima.adapter = pembelianListAdapter
                    }else{
                        b.txtStatusDiterima.text = "Tidak Ada Transaksi"
                    }
                }
            }else{
                b.txtStatusDiterima.text = "Tidak Ada Transaksi"
            }
        })
        vMTransaksi.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressBarDiterima.visibility = View.VISIBLE
                b.recViewDiterima.visibility = View.GONE
                b.txtStatusDiterima.visibility = View.GONE
            }else{
                b.progressBarDiterima.visibility = View.GONE
                b.recViewDiterima.visibility = View.VISIBLE
                b.txtStatusDiterima.visibility = View.VISIBLE
            }
        })
    }

}