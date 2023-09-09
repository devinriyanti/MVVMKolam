package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import id.web.devin.mvvmkolam.databinding.FragmentLoginBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.EncryptionUtils
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.AuthViewModel
import id.web.devin.mvvmkolam.viewmodel.ProfilViewModel

class LoginFragment : Fragment() {
    private lateinit var b: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var vmProfil: ProfilViewModel
    private lateinit var email:String
    private lateinit var pwd :String
    private lateinit var encryptPwd:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentLoginBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.progressBarLogin.visibility = View.GONE
        var role = Global.getRole(requireContext())
        if(context?.let { Global.getEmail(it) } != null){
            if(role == "Admin"){
                startActivity(Intent(context, AdminMainActivity::class.java))
            }else{
                startActivity(Intent(context, MainActivity::class.java))
            }
            activity?.finish()
        }

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        vmProfil = ViewModelProvider(this).get(ProfilViewModel::class.java)
        b.btnLogin.setOnClickListener {
            b.progressBarLogin.visibility = View.VISIBLE
            email = b.editTextEmailLogin.text.toString()
            pwd = b.editTextPwdLogin.text.toString()
            encryptPwd = EncryptionUtils.encrypt(pwd)
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                viewModel.loginUser(email, encryptPwd)
                vmProfil.fetchProfil(email)
                observeView()
            }else{
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Email dan Kata Sandi Tidak Boleh Kosong!")
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
        b.btnDaftar.setOnClickListener {
            val action = LoginFragmentDirections.actionDaftarSebagaiFragment()
            Navigation.findNavController(it).navigate(action)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeView() {
        viewModel.statusLD.observe(viewLifecycleOwner, Observer{
            if(it == true){
                b.progressBarLogin.visibility = View.GONE
                vmProfil.userLD.observe(viewLifecycleOwner, Observer {
                    val role = it.role
                    AlertDialog.Builder(context).apply {
                        val sharedPreferences = context.getSharedPreferences(Global.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString(Global.SHARED_PREF_EMAIL, email)
                        editor.putString(Global.SHARED_PREF_KEY_ROLE,role.toString())
                        editor.apply()
                        val nama = it.nama
                        val title = SpannableString("Login Berhasil")
                        title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                        val message = SpannableString("Selamat Datang $nama!")
                        message.setSpan(
                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0,
                            message.length,
                            0
                        )
                        setTitle(title)
                        setMessage(message)
                        setPositiveButton("OK") { _, _ ->
                            val role = it.role
                            if (role == Role.Pengguna) {
                                var intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                var intent = Intent(context, AdminMainActivity::class.java)
                                startActivity(intent)
                            }
                            activity?.finish()
                        }
                        create().show()
                    }
                })
            }else if(it == false){
                b.txtCekLogin.text = "Email atau Katasandi Salah!"
                b.txtCekLogin.setTextColor(Color.RED)
            }
        })
    }
}