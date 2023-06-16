package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentRincianKolamBinding

class RincianKolamFragment : Fragment() {
    private lateinit var b:FragmentRincianKolamBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b= FragmentRincianKolamBinding.inflate(layoutInflater)
        return b.root
    }
}