package id.web.devin.mvvmkolam.view

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvvmkolam.databinding.FragmentPelatihDetailBinding
import id.web.devin.mvvmkolam.model.Role
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.calculateTotalYears
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.PelatihListViewModel

class PelatihDetailFragment : Fragment() {
    private lateinit var viewModel:PelatihListViewModel
    private lateinit var b: FragmentPelatihDetailBinding

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
        val role = context?.let { Global.getRole(it) }
        if(role == Role.Admin.name){
            b.btnEditPelatihDetail.visibility = View.VISIBLE
            b.btnHapusPelatih.visibility = View.VISIBLE
        }else{
            b.btnEditPelatihDetail.visibility = View.GONE
            b.btnHapusPelatih.visibility = View.GONE
        }

        if(arguments != null){
            val pelatihID = PelatihDetailFragmentArgs.fromBundle(requireArguments()).pelatihID
            viewModel = ViewModelProvider(this).get(PelatihListViewModel::class.java)
            viewModel.refresh(pelatihID)

            observeModel()

            b.btnEditPelatihDetail.setOnClickListener {
                val action = PelatihDetailFragmentDirections.actionPelatihEditFragment(pelatihID)
                Navigation.findNavController(it).navigate(action)
            }

            b.btnHapusPelatih.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Anda yakin ingin menghapus data pelatih?")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Ya"){ dialog,_->
                        viewModel.removeKolam(pelatihID )
                        val action = PelatihDetailFragmentDirections.actionDetailPelatihToDetailKolamFragment()
                        findNavController().navigate(action)
                    }
                    setNegativeButton("Tidak"){ dialog,_->
                        dialog.dismiss()
                    }
                    create().show()
                }
            }
        }
    }

    private fun observeModel() {
        viewModel.pelatihLD.observe(viewLifecycleOwner, Observer {
            val pelatih = viewModel.pelatihLD.value
            pelatih?.let { it->
                Log.d("idp",it.id.toString())
                val thnPengalaman = calculateTotalYears(it.tglKarir.toString())
                val umur = calculateTotalYears(it.tglLahir.toString())
                b.txtNamaPelatihDetail.text = it.nama
                b.txtUmurPelatihDetail.text = "${umur} Tahun"
                b.txtKontakPelatihDetail.text = it.kontak
                b.txtPengalamanPelatihDetail.text = "${thnPengalaman} Tahun"
                b.txtDeskripsiPelatihDetail.text = it.deskripsi
                b.imagePelatihDetail.loadImage(it.gambarUrl.toString(),b.progressBarPelatihDetail)
            }
        })
    }
}