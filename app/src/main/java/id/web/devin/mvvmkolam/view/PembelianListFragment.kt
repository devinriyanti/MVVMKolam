package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import id.web.devin.mvvmkolam.databinding.FragmentPembelianListBinding
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.viewmodel.TransactionViewModel

class PembelianListFragment : Fragment() {
    private lateinit var b:FragmentPembelianListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPembelianListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = b.viewPagerPembelian
        val tabLayout = b.tabLayoutPembelian
        val tabTitles = listOf("Diproses", "Dikirim", "Diterima", "Dibatalkan")

        val adapter = MyStatusAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}