package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentDaftarSebagaiBinding

class DaftarSebagaiFragment : Fragment() {
    private lateinit var b: FragmentDaftarSebagaiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDaftarSebagaiBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.btnAkunPribadi.setOnClickListener {
            val action = DaftarSebagaiFragmentDirections.actionRegisPFragment()
            Navigation.findNavController(it).navigate(action)
        }
        b.btnAkunBisnis.setOnClickListener {
            val action = DaftarSebagaiFragmentDirections.actionRegisBFragment()
            Navigation.findNavController(it).navigate(action)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}