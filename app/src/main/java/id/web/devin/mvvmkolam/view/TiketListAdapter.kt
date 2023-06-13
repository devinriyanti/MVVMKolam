package id.web.devin.mvvmkolam.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.TiketListItemBinding
import id.web.devin.mvvmkolam.model.Tiket
import id.web.devin.mvvmkolam.util.loadImage

class TiketListAdapter(val tiketList:ArrayList<Tiket>):RecyclerView.Adapter<TiketListAdapter.TiketViewHolder>() {
    class TiketViewHolder(var b:TiketListItemBinding):RecyclerView.ViewHolder(b.root)

    fun updateTiketList(newTiketList: ArrayList<Tiket>){
        tiketList.clear()
        tiketList.addAll(newTiketList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiketViewHolder {
        val b = TiketListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TiketViewHolder(b)
    }

    override fun onBindViewHolder(holder: TiketViewHolder, position: Int) {
        if(tiketList[position] != null){
            val tiket = tiketList[position]
            with(holder.b){
                txtTiket.text = tiket.nama
                txtHargaTiket.text = tiket.harga.toString()

                imageView.loadImage(tiket.gambarUrl.toString(), progressBarTiketCard)
                btnBeli.setOnClickListener {
                    val tiketID = tiket.idproduk
                }
            }
        }else{

        }

    }

    override fun getItemCount(): Int {
        return tiketList.size
    }

}