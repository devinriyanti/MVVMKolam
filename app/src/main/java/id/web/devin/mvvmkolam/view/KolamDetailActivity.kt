package id.web.devin.mvvmkolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.ActivityKolamDetailBinding
import id.web.devin.mvvmkolam.databinding.FragmentKolamDetailBinding

class KolamDetailActivity : AppCompatActivity() {
    private lateinit var b:ActivityKolamDetailBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKolamDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.title = "Kolam Renang"
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostDetail) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}