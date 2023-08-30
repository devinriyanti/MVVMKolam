package id.web.devin.mvvmkolam.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentRincianKolamBinding
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel

class RincianKolamFragment : Fragment() {
    private lateinit var b:FragmentRincianKolamBinding
    private lateinit var viewModel:DetailKolamViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b= FragmentRincianKolamBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)

        viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        viewModel.fetchData(id.toString())

        observeView()

    }

    private fun observeView() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            b.txtNamaKolamRIncian.text = it.nama
            b.txtAlamatKolamRincian.text = it.alamat
            b.txtDeskripsiKolamRincian.text = it.deskripsi
            val peta = it.lokasi
            b.txtPeta.setOnClickListener {
                // Membuka browser dengan URL peta
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(peta))
                startActivity(intent)
            }
            b.imageRincianKolam.loadImage(it.gambarUrl.toString(),b.progressBarRincian)
        })
    }
}