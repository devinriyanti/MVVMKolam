package id.web.devin.mvvmkolam.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.HomeListItemBinding
import id.web.devin.mvvmkolam.model.Kolam
import id.web.devin.mvvmkolam.util.loadImage

class KolamListAdapter(val kolamList:ArrayList<Kolam>):RecyclerView.Adapter<KolamListAdapter.KolamViewHolder>() {
    class KolamViewHolder(val binding: HomeListItemBinding):RecyclerView.ViewHolder(binding.root)

    fun updateKolamList(newKolamList:List<Kolam>){
        kolamList.clear()
        kolamList.addAll(newKolamList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KolamViewHolder {
        val binding = HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return KolamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KolamViewHolder, position: Int) {
        val kolam = kolamList[position]
        with(holder.binding){
            txtNamaKolam.text = kolam.nama
            txtAlamatKolam.text = kolam.alamat
            imageKolam.loadImage(kolam.gambarUrl.toString(), progressBar)

            cardListKolam.setOnClickListener {
                val action = HomeListFragmentDirections.actionDetailFragment(kolam.id.toString())
//            Log.d("Show",kolamList[position].id.toString())
                Navigation.findNavController(it).navigate(action)
            }
        }

    }

    override fun getItemCount(): Int {
        return kolamList.size
    }

}