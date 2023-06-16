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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentKolamDetailBinding
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.DetailKolamViewModel

class KolamDetailFragment : Fragment() {
    private lateinit var viewModel: DetailKolamViewModel
    private lateinit var b: FragmentKolamDetailBinding
    private lateinit var navController: NavController
    private var kolamID:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentKolamDetailBinding.inflate(layoutInflater)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = b.viewPagerTab
        val tabLayout = b.tabLayout

        //Setup ViewPager
        val adapter = MyPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager){tab, position->
            when (position) {
                0 -> tab.text = "Tiket"
                1 -> tab.text = "Produk"
                2 -> tab.text = "Pelatih"
            }
        }

        if(arguments != null){
            kolamID = KolamDetailFragmentArgs.fromBundle(requireArguments()).kolamID
            viewModel = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
//            Log.d("idkolam",kolamID)
            viewModel.fetchData(kolamID!!)

            observeModel()
            navController = Navigation.findNavController(requireParentFragment().requireView())

        }
    }

    private fun observeModel() {
        viewModel.kolamLD.observe(viewLifecycleOwner, Observer {
            val id = it.id.toString()
            b.txtNamaKolamDetail.setText(it.nama)
            b.imageKolamDetail.loadImage(it.gambarUrl.toString(),b.progressBarDetailKolam)
            b.txtNamaKolamDetail.setOnClickListener {
                val action = KolamDetailFragmentDirections.actionRincianKolamFragment(id)
                Navigation.findNavController(it).navigate(action)
            }

        })
    }
}