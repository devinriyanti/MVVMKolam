package id.web.devin.mvvmkolam.view

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentProfilEditBinding
import id.web.devin.mvvmkolam.model.Gender
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.formatDate
import id.web.devin.mvvmkolam.util.formatDate2
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel
import java.text.SimpleDateFormat
import java.util.*

class ProfilEditFragment : Fragment() {
    private lateinit var b:FragmentProfilEditBinding
    private lateinit var viewModel: ProfilViewModel
    private lateinit var nama:String
    private lateinit var noTelp:String
    private lateinit var gender:String
    private lateinit var alamat:String
    private lateinit var tglLahir:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentProfilEditBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = context?.let { Global.getEmail(it) }.toString()

        //Set Spinner
        val jenis = arrayOf(Gender.Other.displayText,Gender.Laki_Laki.displayText, Gender.Perempuan.displayText)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, jenis)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        b.spinnerGender.adapter = adapter

        b.spinnerGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                gender = jenis[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel = ViewModelProvider(this).get(ProfilViewModel::class.java)
        viewModel.fetchProfil(email)
        observeView()

        b.txtTglLahirProfil.setOnClickListener {
            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DAY_OF_MONTH)

            var picker = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                        val calender = Calendar.getInstance()
                        calender.set(selYear,selMonth,selDay)

                        var dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                        var str = dateFormat.format(calender.time)
                        b.txtTglLahirProfil.setText(str)
                    }, year, month, day)
            }
            picker?.show()
        }

        b.btnBatalEditProfil.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Batal Melakukan Perubahan?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("BATAL"){ dialog,_->
                    val action = ProfilEditFragmentDirections.actionItemDataDiri()
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("TIDAK"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }

        b.btnSimpanProfil.setOnClickListener {""
            nama = b.txtNamaProfil.text.toString()
            noTelp = b.txtTeleponProfil.text.toString()
            alamat = b.txtAlamatProfil.text.toString()
            Log.d("gender",gender)
            tglLahir = formatDate2(b.txtTglLahirProfil.text.toString())
            viewModel.updateUser(email,nama,alamat,noTelp,gender,tglLahir)
            updateUser()
        }
    }

    private fun updateUser() {
        viewModel.statusLD.observe(viewLifecycleOwner, Observer {success->
            if(success == true){
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Data Diri Berhasil Diubah")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK") { _, _ ->
                        val action = ProfilEditFragmentDirections.actionItemDataDiri()
                        findNavController().navigate(action)
                    }
                    create().show()
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Gagal Melakukan Pembaruan Data Diri")
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


    private fun observeView() {
        viewModel.userLD.observe(viewLifecycleOwner, Observer {
            //Nama
            if(!it.nama.isNullOrEmpty()){
                b.txtNamaProfil.setText(it.nama)
            }else{
                b.txtNamaProfil.setHint("Belum Diatur")
            }

            //Email
            if(!it.email.isNullOrEmpty()){
                b.txtEmailProfil.setText(it.email)
            }else{
                b.txtEmailProfil.setHint("Belum Diatur")
            }

            //Telepon
            if(!it.telepon.isNullOrEmpty()){
                b.txtTeleponProfil.setText(it.telepon)
            }else{
                b.txtTeleponProfil.setHint("Belum Diatur")
            }

            //Alamat
            if(!it.alamat.isNullOrEmpty()){
                b.txtAlamatProfil.setText(it.alamat)
            }else{
                b.txtAlamatProfil.setHint("Belum Diatur")
            }
//            val k = it.jenis_kelamin.displayText
            //Jenis Kelamin
            if(!it.jenis_kelamin.isNullOrEmpty()){
                if(it.jenis_kelamin == Gender.Laki_Laki.displayText){
                    b.spinnerGender.setSelection(1)
                }else if(it.jenis_kelamin == Gender.Perempuan.displayText){
                    b.spinnerGender.setSelection(2)
                }

            }else{
                b.spinnerGender.setSelection(0)
            }

            //Tanggal Lahir
            if(!it.tglLahir.isNullOrEmpty()){
                val tgl = formatDate(it.tglLahir.toString())
                b.txtTglLahirProfil.setText(tgl)
            }else{
                b.txtTglLahirProfil.setHint("Belum Diatur")
            }
        })
    }
}