package id.web.devin.mvvmkolam.view

import android.app.Activity
import android.app.AlertDialog
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
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentProfileBinding
import id.web.devin.mvvmkolam.model.Gender
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.formatDate
import id.web.devin.mvvmkolam.viewmodel.AuthViewModel
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel

class ProfileFragment : Fragment() {
    private lateinit var b:FragmentProfileBinding
    private lateinit var viewModel:ProfilViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = context?.let { Global.getEmail(it) }
        viewModel = ViewModelProvider(this)[ProfilViewModel::class.java]
        viewModel.fetchProfil(email.toString())
        observeView()
        b.btnEditProfil.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfilEditFragment()
            Navigation.findNavController(it).navigate(action)
        }

        b.btnLogout.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Apakah Anda Yakin Akan Keluar?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("YA"){ _,_->
                    requireActivity().getSharedPreferences(Global.SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                        .edit()
                        .remove(Global.SHARED_PREF_EMAIL).apply()
                    requireActivity().getSharedPreferences(Global.SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                        .edit()
                        .remove(Global.SHARED_PREF_KEY_ROLE).apply()
                    requireActivity().finish()
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                }
                setNegativeButton("TIDAK"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
        b.btnUbahPwd.setOnClickListener {
            val action = ProfileFragmentDirections.actionPasswordEditFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun observeView() {
        viewModel.userLD.observe(viewLifecycleOwner, Observer {
            //Nama
            if(!it.nama.isNullOrEmpty()){
                b.txtNamaProfil.setText(it.nama)
            }else{
                b.txtNamaProfil.setText("Belum diatur")
            }

            //Email
            if(!it.email.isNullOrEmpty()){
                b.txtEmailProfil.setText(it.email)
            }else{
                b.txtEmailProfil.setText("Belum diatur")
            }

            //Telepon
            if(!it.telepon.isNullOrEmpty()){
                b.txtTeleponProfil.setText(it.telepon)
            }else{
                b.txtTeleponProfil.setText("Belum diatur")
            }

            //Alamat
            if(!it.alamat.isNullOrEmpty()){
                b.txtAlamatProfil.setText(it.alamat)
            }else{
                b.txtAlamatProfil.setText("Belum diatur")
            }

//            Log.d("JenisK",it.jenis_kelamin.displayText)
            //Jenis Kelamin
            if(!it.jenis_kelamin.isNullOrEmpty()){
                b.txtGenderProfil.setText(it.jenis_kelamin)
            }else{
                b.txtGenderProfil.setText("Belum diatur")
            }

            //Tanggal Lahir
            if(!it.tglLahir.isNullOrEmpty()){
                val tgl = formatDate(it.tglLahir.toString())
                b.txtTanggalLahir.setText(tgl)
            }else{
                b.txtTanggalLahir.setText("Belum diatur")
            }
        })
    }
}