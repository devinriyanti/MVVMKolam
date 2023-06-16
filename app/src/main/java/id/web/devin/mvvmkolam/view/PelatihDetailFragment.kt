package id.web.devin.mvvmkolam.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.web.devin.mvvmkolam.databinding.FragmentPelatihDetailBinding
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.PelatihListViewModel

class PelatihDetailFragment : Fragment() {
    private lateinit var viewModel:PelatihListViewModel
    private lateinit var b:FragmentPelatihDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPelatihDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null){
            val pelatihID = PelatihDetailFragmentArgs.fromBundle(requireArguments()).pelatihID
            viewModel = ViewModelProvider(this).get(PelatihListViewModel::class.java)

            observeModel()
        }
    }

    private fun observeModel() {
        viewModel.pelatihLD.observe(viewLifecycleOwner, Observer {
            val pelatih = viewModel.pelatihLD.value
            pelatih?.let { it->
                Log.d("idp",it.id.toString())
                b.txtNamaPelatihDetail.text = it.nama
                b.txtUmurPelatihDetail.text = it.tglLahir
                b.txtKontakPelatihDetail.text = it.kontak
                b.txtPengalamanPelatihDetail.text = it.tglKarir
                b.txtDeskripsiPelatihDetail.text = it.deskripsi
                b.imagePelatihDetail.loadImage(it.gambarUrl.toString(),b.progressBarPelatihDetail)
            }
        })
    }
}