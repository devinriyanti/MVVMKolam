package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentRincianKolamBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel
import id.web.devin.mvvmkolam.viewmodel.KolamListViewModel

class RincianKolamFragment : Fragment() {
    private lateinit var b:FragmentRincianKolamBinding
    private lateinit var viewModel:DetailKolamViewModel
    private lateinit var vMKolam:KolamListViewModel
    private var role:String =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b= FragmentRincianKolamBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        vMKolam = ViewModelProvider(this).get(KolamListViewModel::class.java)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        Log.d("id",id.toString())

        role = context?.let { Global.getRole(it) }.toString()
        if(role == Role.Admin.name){
            b.btnEditKolamRincian.visibility = View.VISIBLE
            b.btnHapusKolam.visibility = View.VISIBLE
            b.switchMaintenance.visibility = View.VISIBLE
            b.switchStatusToko.visibility = View.VISIBLE
        }else{
            b.btnEditKolamRincian.visibility = View.GONE
            b.btnHapusKolam.visibility = View.GONE
            b.switchMaintenance.visibility = View.GONE
            b.switchStatusToko.visibility = View.GONE
        }

        b.switchMaintenance.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vMKolam.updateMaintenance(0,id.toString())
            } else {
                vMKolam.updateMaintenance(1,id.toString())
            }
        }

        b.switchStatusToko.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Anda yakin ingin menutup kolam secara permanen?")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Ya"){ dialog,_->
                        vMKolam.updateStatus(1,id.toString())
                    }
                    setNegativeButton("Tidak"){ dialog,_->
                        dialog.dismiss()
                    }
                    create().show()
                }
            } else {
                vMKolam.updateStatus(0,id.toString())
            }
        }

        b.btnEditKolamRincian.setOnClickListener {
            val action = RincianKolamFragmentDirections.actionKolamEditFragment()
            Navigation.findNavController(it).navigate(action)
        }

        b.btnHapusKolam.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Anda yakin ingin menghapus data kolam?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("Ya"){ dialog,_->
                    vMKolam.removeKolam(id.toString())
                    val intent = Intent(context,AdminMainActivity::class.java)
                    startActivity(intent)
                }
                setNegativeButton("Tidak"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
        viewModel.fetchData(id.toString())

        observeView()
    }

    private fun observeView() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            b.switchMaintenance.isChecked = it.is_maintenance.equals("0")
            b.switchStatusToko.isChecked = it.status.equals("1")
            b.txtNamaKolamRIncian.text = it.nama
            b.txtAlamatKolamRincian.text = it.alamat
            b.txtDeskripsiKolamRincian.text = it.deskripsi
            val peta = it.lokasi
            b.txtPeta.setOnClickListener {
                // Membuka browser dengan URL peta
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(peta))
                startActivity(intent)
            }
            b.imageRincianKolam.loadImage(it.gambarUrl.toString(),b.progressBarRincian)
        })
    }
}