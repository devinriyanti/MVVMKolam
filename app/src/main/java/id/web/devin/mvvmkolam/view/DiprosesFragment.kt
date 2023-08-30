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
import id.web.devin.mvvmkolam.databinding.FragmentDiprosesBinding
import id.web.devin.mvvmkolam.databinding.FragmentPembelianListBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class DiprosesFragment : Fragment() {
    private lateinit var b: FragmentDiprosesBinding
    private lateinit var pembelianListAdapter: PembelianListAdapter
    private lateinit var vMTransaksi: TransactionViewModel
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDiprosesBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        pembelianListAdapter = PembelianListAdapter(arrayListOf())
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)
        val role = Global.getRole(requireContext())
        if(role.equals(Role.Admin.name)){
            vMTransaksi.fetchTransaksiAdmin(email, StatusTransaksi.Diproses.name)
        }else{
            Log.d("masuk", "masuk sini")
            vMTransaksi.fetchTransaksiList(email, StatusTransaksi.Diproses.name)
        }
        observeView()
    }

    private fun observeView() {
        vMTransaksi.transaksiLD.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                pembelianListAdapter.updateTransactionList(it)
                it.forEach {
                    if(!it.id.isNullOrEmpty()){
                        b.recViewDiproses.layoutManager = LinearLayoutManager(context)
                        b.recViewDiproses.adapter = pembelianListAdapter
                    }else{
                        b.txtStatusDiproses.text = "Tidak Ada Transaksi"
                    }
                }
            }else{
                b.txtStatusDiproses.text = "Tidak Ada Transaksi"
            }
        })
        vMTransaksi.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressBarDiproses.visibility = View.VISIBLE
                b.recViewDiproses.visibility = View.GONE
                b.txtStatusDiproses.visibility = View.GONE
            }else{
                b.progressBarDiproses.visibility = View.GONE
                b.recViewDiproses.visibility = View.VISIBLE
                b.txtStatusDiproses.visibility = View.VISIBLE
            }
        })
    }
}