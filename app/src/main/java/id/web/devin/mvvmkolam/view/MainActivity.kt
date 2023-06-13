package id.web.devin.mvvmkolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)

        setContentView(b.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostMainFragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        b.bottomNav.setupWithNavController(navController)
    }




}