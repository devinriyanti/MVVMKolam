package id.web.devin.mvvmkolam.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.CartItemBinding
import id.web.devin.mvvmkolam.databinding.CartListItemBinding
import id.web.devin.mvvmkolam.model.Cart
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.util.loadImage
import id.web.devin.mvvmkolam.viewmodel.CartViewModel

class CartListAdapter(val cartList: ArrayList<Cart>) : RecyclerView.Adapter<CartListAdapter.CartViewHolder>() {
    class CartViewHolder(val binding: CartListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var viewModel: CartViewModel

    fun updateCartList(newCartList: List<Cart>) {
        cartList.clear()
        cartList.addAll(newCartList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartList[position]
        with(holder.binding){
            txtNamaKolamCart.text = cart.nama
            Log.d("cart","uhuhu")
            Log.d("kolam","cart.nama.toString()")

            btnCheckout.setOnClickListener {
//                val action
//                Navigation.findNavController(it).navigate(action)
            }
//            viewModel = ViewModelProvider(ow).get(CartViewModel::class.java)
//            val vM = ViewModelProvider(this).get(CartViewModel::class.java)
            val cartItemAdapter = CartItemAdapter(arrayListOf())
            cartItemRecView.layoutManager = LinearLayoutManager(root.context)
            cartItemRecView.adapter = cartItemAdapter
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}