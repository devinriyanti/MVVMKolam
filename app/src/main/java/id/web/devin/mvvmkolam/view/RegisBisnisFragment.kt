package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.content.Context
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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import id.web.devin.mvvmkolam.databinding.FragmentRegisBisnisBinding
import id.web.devin.mvvmkolam.util.EncryptionUtils
import id.web.devin.mvvmkolam.viewmodel.AuthViewModel

class RegisBisnisFragment : Fragment() {
    private lateinit var b:FragmentRegisBisnisBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var nama:String
    private lateinit var email:String
    private lateinit var telepon:String
    private lateinit var alamat:String
    private lateinit var norekening:String
    private lateinit var namarekening:String
    private lateinit var pwd:String
    private lateinit var pwdKonfirmasi:String
    private lateinit var role:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentRegisBisnisBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        b.btnDaftarBisnis.setOnClickListener {
            nama = b.txtNamaBisnis.text.toString()
            email = b.txtEmailBisnis.text.toString()
            pwd = b.txtKSBisnis.text.toString()
            alamat = b.txtAlamatBisnis.text.toString()
            norekening = b.txtNoRekeningBisnis.text.toString()
            namarekening = b.txtNamaRekeningBisnis.text.toString().toUpperCase()
            val encryptPwd = EncryptionUtils.encrypt(pwd)
            pwdKonfirmasi = b.txtKonKSBisnis.text.toString()
            telepon = b.txtNoTeleponBisnis.text.toString()
            val idkota = "444"
            role = "Admin"
            viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

            if(nama.isNotEmpty() && email.isNotEmpty() && pwd.isNotEmpty() && telepon.isNotEmpty() && pwdKonfirmasi.isNotEmpty() && alamat.isNotEmpty() && norekening.isNotEmpty() && namarekening.isNotEmpty()){
                if(pwd.equals(pwdKonfirmasi)){
                    if(pwd.length >= 4 && pwd.length <=8){
                        viewModel.registerAdmin(nama, email, alamat, norekening, namarekening, telepon, encryptPwd, role, idkota)
                        observeView()
                    }else{
                        Toast.makeText(context, "Kata Sandi Harus Menggunakan 4-8 Karakter!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    AlertDialog.Builder(context).apply {
                        val message = SpannableString("Kata Sandi Tidak Cocok Dengan Konfimasi Kata Sandi")
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

        b.txtMasukBisnistoLogin.setOnClickListener {
            val action = RegisBisnisFragmentDirections.actionRBLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeView() {
        viewModel.statusLD.observe(viewLifecycleOwner, Observer {
            if(it == true){
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Berhasil Melakukan Registrasi")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK") { _, _ ->
                        var intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    create().show()
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Gagal Melakukan Registrasi")
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