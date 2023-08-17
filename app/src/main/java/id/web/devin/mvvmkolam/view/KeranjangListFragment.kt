package id.web.devin.mvvmkolam.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.web.devin.mvvmkolam.util.Global
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.CartListItemBinding
import id.web.devin.mvvmkolam.databinding.FragmentKeranjangListBinding
import id.web.devin.mvvmkolam.model.Cart
import id.web.devin.mvvmkolam.model.ProdukCart
import id.web.devin.mvvmkolam.viewmodel.CartViewModel

class KeranjangListFragment : Fragment(){
    private lateinit var b:FragmentKeranjangListBinding
    private lateinit var viewModel: CartViewModel
    private val cartListAdapter= CartListAdapter(arrayListOf())
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentKeranjangListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        b.txtErrorCart.visibility = View.GONE
//        Log.d("emailCart",email)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        viewModel.fetchCartList(email)

        observeView()
    }

    private fun observeView() {
        viewModel.cartLD.observe(viewLifecycleOwner, Observer {
            Log.d("cartLD",it.toString())
            if(!it.isNullOrEmpty()){

                cartListAdapter.updateCartList(it)
//                Log.d("dasda",Car)
//                cartItemAdapter.updateCartItem(it)
                b.textIsiKeranjang.text = ""

                b.refreshCart.setOnRefreshListener {
                    b.recyclerViewCart.visibility = View.GONE
                    b.txtErrorCart.visibility = View.GONE
                    b.textIsiKeranjang.visibility = View.GONE
                    b.progressBarCart.visibility = View.VISIBLE
                    viewModel.fetchCartList(email)
                    b.refreshCart.isRefreshing = false
                }
                b.recyclerViewCart.layoutManager = LinearLayoutManager(context)
                b.recyclerViewCart.adapter = cartListAdapter
            }else{
                b.textIsiKeranjang.text = "Keranjang Kosong"
            }
        })
        viewModel.loadingErrorLD.observe(viewLifecycleOwner, Observer {
            b.txtErrorCart.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressBarCart.visibility = View.VISIBLE
                b.recyclerViewCart.visibility = View.GONE
                b.textIsiKeranjang.visibility = View.GONE
            }else{
                b.progressBarCart.visibility = View.GONE
                b.recyclerViewCart.visibility = View.VISIBLE
                b.textIsiKeranjang.visibility = View.VISIBLE
            }
        })
    }

    private inner class CartListAdapter(val cartList: ArrayList<Cart>) : RecyclerView.Adapter<CartListAdapter.CartViewHolder>() {
        private inner class CartViewHolder(val binding: CartListItemBinding) : RecyclerView.ViewHolder(binding.root)

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
                val kolam = cart.nama
                txtNamaKolamCart.text = kolam
                btnCheckout.setOnClickListener {
                    var intent = Intent(context, CheckoutActivity::class.java)
                    startActivity(intent)
                }
                cart.produk.forEach {
                    if (cart.id == it.idkolam){

                        val cartItemAdapter = CartItemAdapter(arrayListOf())
                        cartItemRecView.layoutManager = LinearLayoutManager(root.context)
                        cartItemRecView.adapter = cartItemAdapter

                        cartItemAdapter.updateCartItem(cart.produk)
                        Log.d("produkList",cart.produk.toString())
                    }
                }
            }
        }
        override fun getItemCount(): Int {
            return cartList.size
        }
    }
}