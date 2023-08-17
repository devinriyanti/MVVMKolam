package id.web.devin.mvvmkolam.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.PelatihListItemBinding
import id.web.devin.mvvmkolam.model.Pelatih
import id.web.devin.mvvmkolam.util.calculateTotalYears
import id.web.devin.mvvmkolam.util.loadImage
import java.util.Calendar
import java.util.Date

class PelatihListAdapter(val pelatihList:ArrayList<Pelatih>):RecyclerView.Adapter<PelatihListAdapter.PelatihViewHolder>() {
    class PelatihViewHolder(val b:PelatihListItemBinding):RecyclerView.ViewHolder(b.root)

    fun updatePelatihList(newPelatihList:List<Pelatih>){
        pelatihList.clear()
        pelatihList.addAll(newPelatihList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelatihViewHolder {
        val b = PelatihListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PelatihViewHolder(b)
    }

    override fun getItemCount(): Int {
        return pelatihList.size
    }

    override fun onBindViewHolder(holder: PelatihViewHolder, position: Int) {
        val pelatih = pelatihList[position]
        with(holder.b){
            txtNamaPelatih.text = pelatih.nama
            val thnPengalaman = calculateTotalYears(pelatih.tglKarir.toString())
            txtTahunPengalaman.text = "Pengalaman: ${thnPengalaman} Tahun"
            val id = pelatih.id.toString()
            imageViewPelatih.loadImage(pelatih.gambarUrl.toString(), progressBarPelatih)

            cardPelatih.setOnClickListener {
                val action = KolamDetailFragmentDirections.actionPelatihDetailFragment(id)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
}