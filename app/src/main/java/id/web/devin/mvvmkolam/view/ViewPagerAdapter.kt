package id.web.devin.mvvmkolam.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0->TiketListFragment()
            1->ProductListFragment()
            2->PelatihListFragment()
            else-> throw IllegalArgumentException("Invalid Tab Position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Tiket"
            1 -> "Produk"
            2 -> "Pelatih"
            else -> null
        }
    }

}