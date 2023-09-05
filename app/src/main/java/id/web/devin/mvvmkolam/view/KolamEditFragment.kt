package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentKolamEditBinding
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel
import id.web.devin.mvvmkolam.viewmodel.KolamListViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class KolamEditFragment : Fragment() {
    private lateinit var b:FragmentKolamEditBinding
    private lateinit var vMKolam:KolamListViewModel
    private lateinit var vMKolamDetai:DetailKolamViewModel
    private var namaKolam:String = ""
    private var alamat:String = ""
    private var deskripsi:String = ""
    private var urlLokasi:String = ""
    private var kolamID:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentKolamEditBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vMKolam = ViewModelProvider(this).get(KolamListViewModel::class.java)
        vMKolamDetai = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        kolamID = id.toString()

        vMKolamDetai.fetchData(kolamID)
        observeView()

        b.btnEditKolam.setOnClickListener {
            namaKolam = b.editTextNamaKolamEdit.text.toString()
            alamat = b.editTextAlamatKolamEdit.text.toString()
            deskripsi = b.editTextDeskripsiKolamEdit.text.toString()
            urlLokasi = b.editTextLokasiKolamEDit.text.toString()

            if(namaKolam.isNotEmpty() && alamat.isNotEmpty() && deskripsi.isNotEmpty() && urlLokasi.isNotEmpty()){
                vMKolam.updateKolam(namaKolam,alamat,deskripsi,urlLokasi,kolamID)
                updateKolam()
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Data Tidak Boleh Kosong!")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK", null)
                    create().show()
                }
            }
        }
        b.btnBatalEditKolam.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Batal Mengubah Data Kolam?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("BATAL"){ dialog,_->
                    val action = KolamEditFragmentDirections.actionEditToRincianKolamFragment(kolamID)
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("TIDAK"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
    }

    private fun observeView() {
        vMKolamDetai.kolamLD.observe(viewLifecycleOwner, Observer {
            b.editTextNamaKolamEdit.setText(it.nama)
            b.editTextAlamatKolamEdit.setText(it.alamat)
            b.editTextDeskripsiKolamEdit.setText(it.deskripsi)
            b.editTextLokasiKolamEDit.setText(it.lokasi)
        })
    }

    private fun updateKolam() {
        vMKolam.statusLD.observe(viewLifecycleOwner, Observer {
            if(it == true){
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Berhasil Mengubah Data Kolam")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK") { _, _ ->
                        val action = KolamEditFragmentDirections.actionEditToRincianKolamFragment()
                        findNavController().navigate(action)
                    }
                    create().show()
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Gagal Mengubah Data Kolam")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK", null)
                    create().show()
                }
            }
        })
    }
}