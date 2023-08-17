package id.web.devin.mvvmkolam.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.ProductListItemBinding
import id.web.devin.mvvmkolam.model.Produk
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.util.loadImage
import kotlin.math.roundToLong

class ProductListAdapter(val produkList:ArrayList<Produk>):RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    class ProductViewHolder(val b:ProductListItemBinding):RecyclerView.ViewHolder(b.root)

    fun updateProductList(newProductList:ArrayList<Produk>){
        produkList.clear()
        produkList.addAll(newProductList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val b = ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(b)
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val produk = produkList[position]

        with(holder.b){
            txtNamaProduk.text = produk.nama
            val harga = produk?.harga?.let { formatCurrency(it) }
            txtHargaProduk.text = harga
            txtKota.text = produk.kota
            val id = produk.idproduk.toString()
            imageProduct.loadImage(produk.gambarUrl.toString(), progressBarProduk)

            cardViewProduk.setOnClickListener {
                val action = KolamDetailFragmentDirections.actionProductDetailFragment(id)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }


}