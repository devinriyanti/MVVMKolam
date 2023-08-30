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
import id.web.devin.mvvmkolam.databinding.FragmentDibatalkanBinding
import id.web.devin.mvvmkolam.databinding.FragmentDikirimBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class DikirimFragment : Fragment() {
    private lateinit var b: FragmentDikirimBinding
    private lateinit var pembelianListAdapter: PembelianListAdapter
    private lateinit var vMTransaksi: TransactionViewModel
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDikirimBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        pembelianListAdapter = PembelianListAdapter(arrayListOf())
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)
        val role = Global.getRole(requireContext())
        if(role == Role.Admin.name){
            vMTransaksi.fetchTransaksiAdmin(email,StatusTransaksi.Dikirim.name)
        }else{
            vMTransaksi.fetchTransaksiList(email,StatusTransaksi.Dikirim.name)
        }

        observeView()
    }

    private fun observeView() {
        vMTransaksi.transaksiLD.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                pembelianListAdapter.updateTransactionList(it)
                it.forEach {
                    if(!it.id.isNullOrEmpty()){
                        b.recViewDikirim.layoutManager = LinearLayoutManager(context)
                        b.recViewDikirim.adapter = pembelianListAdapter
                    }else{
                        b.txtStatusDikirim.text = "Tidak Ada Transaksi"
                    }
                }
            }else{
                b.txtStatusDikirim.text = "Tidak Ada Transaksi"
            }
        })
        vMTransaksi.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressBarDikirim.visibility = View.VISIBLE
                b.recViewDikirim.visibility = View.GONE
                b.txtStatusDikirim.visibility = View.GONE
            }else{
                b.progressBarDikirim.visibility = View.GONE
                b.recViewDikirim.visibility = View.VISIBLE
                b.txtStatusDikirim.visibility = View.VISIBLE
            }
        })
    }

}