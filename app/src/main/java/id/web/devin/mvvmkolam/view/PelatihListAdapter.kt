package id.web.devin.mvvmkolam.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.PelatihListItemBinding
import id.web.devin.mvvmkolam.model.Pelatih
import id.web.devin.mvvmkolam.util.loadImage
import java.util.Calendar
import java.util.Date

class PelatihListAdapter(val pelatihList:ArrayList<Pelatih>):RecyclerView.Adapter<PelatihListAdapter.PelatihViewHolder>() {
    class PelatihViewHolder(val b:PelatihListItemBinding):RecyclerView.ViewHolder(b.root)

    fun updatePelatihList(newPelatihList:ArrayList<Pelatih>){
        pelatihList.clear()
        pelatihList.addAll(newPelatihList)
        notifyDataSetChanged()
    }

    fun tahunPengalamanKerja(tglMulaiKarir: Date, tglSekarang:Date): Int{
        val calendar = Calendar.getInstance()
        calendar.time = tglMulaiKarir
        val tahunMulaiKarir = calendar.get(Calendar.YEAR)

        calendar.time = tglSekarang
        val tahunSekarang = calendar.get(Calendar.YEAR)

        // Memastikan rentang tanggal valid
        if (tahunMulaiKarir > tahunSekarang) {
            throw IllegalArgumentException("Invalid date range")
        }

        return tahunMulaiKarir
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
            txtTahunPengalaman.text = pelatih.tglKarir
            Log.d("url",pelatih.gambarUrl.toString())
            val id = pelatih.id.toString()
            Log.d("idpelatih",id)
            imageViewPelatih.loadImage(pelatih.gambarUrl.toString(), progressBarPelatih)

            cardPelatih.setOnClickListener {
                val action = KolamDetailFragmentDirections.actionPelatihDetailFragment(id)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
}