package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentPasswordEditBinding
import id.web.devin.mvvmkolam.util.EncryptionUtils
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel

class PasswordEditFragment : Fragment() {
    private lateinit var b:FragmentPasswordEditBinding
    private lateinit var vMProfil:ProfilViewModel
    private var pwd:String = ""
    private var newPwd:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPasswordEditBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vMProfil = ViewModelProvider(this).get(ProfilViewModel::class.java)
        val email = context?.let { Global.getEmail(it) }
        val encryptPwd = EncryptionUtils.encrypt(pwd)
        b.btnSimpanEditPwd.setOnClickListener {
            pwd = b.txtEditKataSandi.text.toString()
            newPwd = b.txtEditKonfirmasiKataSandi.text.toString()
            if(pwd.isNotEmpty() && newPwd.isNotEmpty()){
                if (pwd.equals(newPwd)){
                    if(pwd.length >= 4 && pwd.length <=8){
                        vMProfil.updateKatasandi(email.toString(),encryptPwd)
                        observeView()
                    }else{
                        Toast.makeText(context, "Kata Sandi Harus Menggunakan 4-8 Karakter!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    AlertDialog.Builder(context).apply {
                        val title = SpannableString("Peringatan")
                        title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                        val message = SpannableString("Kata Sandi Tidak Cocok Dengan Konfimasi Kata Sandi")
                        message.setSpan(
                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0,
                            message.length,
                            0
                        )
                        setTitle(title)
                        setMessage(message)
                        setPositiveButton("OK", null)
                        create().show()
                    }
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Data Tidak Boleh Kosong")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("OK", null)
                    create().show()
                }
            }
        }
        b.btnBatalEditKataSandi.setOnClickListener {
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
                    val action = PasswordEditFragmentDirections.actionEditPwdToItemDataDiri()
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
        vMProfil.statusLD.observe(viewLifecycleOwner, Observer {
            if(it == true){
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Berhasil Mengubah Kata Sandi")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK") { _, _ ->
                        val action = PasswordEditFragmentDirections.actionEditPwdToItemDataDiri()
                        findNavController().navigate(action)
                    }
                    create().show()
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Gagal Mengubah Kata Sandi")
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