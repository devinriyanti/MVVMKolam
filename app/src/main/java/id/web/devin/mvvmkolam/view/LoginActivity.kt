package id.web.devin.mvvmkolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var b:ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate((layoutInflater))
        setContentView(b.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostLoginFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
}