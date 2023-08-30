package id.web.devin.mvvmkolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.ActivityPembelianDetailBinding
import id.web.devin.mvvmkolam.databinding.FragmentPembelianDetailBinding

class PembelianDetailActivity : AppCompatActivity() {
    private lateinit var b:ActivityPembelianDetailBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPembelianDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostPembelianDetail) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }

}