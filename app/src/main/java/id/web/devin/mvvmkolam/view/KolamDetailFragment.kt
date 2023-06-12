package id.web.devin.mvvmkolam.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentKolamDetailBinding
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel

class KolamDetailFragment : Fragment() {
    private lateinit var viewModel: DetailKolamViewModel
    private lateinit var b: FragmentKolamDetailBinding
    private lateinit var navController: NavController
    private val tiketListAdapter = TiketListAdapter(arrayListOf())
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentKolamDetailBinding.inflate(layoutInflater)
        val adapter = ViewPagerAdapter(childFragmentManager)
        b.viewPagerTab.adapter = adapter
        b.tabLayout.setupWithViewPager(b.viewPagerTab)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            val kolamID = KolamDetailFragmentArgs.fromBundle(requireArguments()).kolamID
            viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
//            Log.d("idkolam",kolamID)
            viewModel.fetchData(kolamID)

//            b.recViewTiket.layoutManager = LinearLayoutManager(context)
//            b.recViewTiket.adapter = tiketListAdapter

            observeModel()
            navController = Navigation.findNavController(requireParentFragment().requireView())
        }
    }

    private fun observeModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            tiketListAdapter.updateTiketList(it.tiket)
            b.txtNamaKolamDetail.setText(it.nama)
            b.imageKolamDetail.loadImage(it.gambarUrl.toString(),b.progressBarDetailKolam)
        })
    }
}