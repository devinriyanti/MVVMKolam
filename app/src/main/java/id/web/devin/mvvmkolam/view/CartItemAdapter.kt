package id.web.devin.mvvmkolam.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.CartItemBinding
import id.web.devin.mvvmkolam.model.ProdukCart
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.util.loadImage

class CartItemAdapter(val cartItemList:ArrayList<ProdukCart>):RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {
    class CartItemViewHolder(val b:CartItemBinding):RecyclerView.ViewHolder(b.root)

    fun updateCartItem(newCartItem:ArrayList<ProdukCart>){
        cartItemList.clear()
        cartItemList.addAll(newCartItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val b = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartItemViewHolder(b)
    }

    override fun getItemCount(): Int {
        Log.d("size", cartItemList.size.toString())
        return cartItemList.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        with(holder.b){
            txtNamaProdukCart.text = cartItem.namaProduk
            val harga = cartItem.harga?.let { formatCurrency(it) }
            txtHargaProdukCart.text = harga
            txtQtyProdukCart.text = cartItem.qty.toString()
            imgProdukCart.loadImage(cartItem.gambar,progressLoadCartItem)
        }
    }
}