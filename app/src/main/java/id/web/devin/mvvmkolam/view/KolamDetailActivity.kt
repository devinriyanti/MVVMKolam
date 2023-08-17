package id.web.devin.mvvmkolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.ActivityKolamDetailBinding
import id.web.devin.mvvmkolam.databinding.FragmentKolamDetailBinding

class KolamDetailActivity : AppCompatActivity() {
    private lateinit var b:ActivityKolamDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKolamDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}