package id.web.devin.mvvmkolam.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.web.devin.mvvmkolam.util.Global
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvvmkolam.databinding.CartListItemBinding
import id.web.devin.mvvmkolam.databinding.FragmentKeranjangListBinding
import id.web.devin.mvvmkolam.model.Cart
import id.web.devin.mvvmkolam.model.ProdukCart
import id.web.devin.mvvmkolam.viewmodel.CartViewModel

class KeranjangListFragment : Fragment(), CartItemAdapter.CartItemListener {
    private lateinit var b:FragmentKeranjangListBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var cartListAdapter: CartListAdapter
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
        cartListAdapter = CartListAdapter(requireContext(), arrayListOf(), this)
        email = context?.let { Global.getEmail(it) }.toString()
        b.txtErrorCart.visibility = View.GONE
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModel.fetchCartList(email)

        observeView()
    }

    private fun observeView() {
        viewModel.cartLD.observe(viewLifecycleOwner, Observer {
            Log.d("cartLD",it.toString())
            if(!it.isNullOrEmpty()){

                cartListAdapter.updateCartList(it)
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
        viewModel.statusRemoveLD.observe(viewLifecycleOwner, Observer {
            if(it == false){
                refreshCartList()
            }
        })
    }

    private fun refreshCartList() {
        viewModel.fetchCartList(email)
    }

    override fun onRemoveClicked(cartItem: ProdukCart) {
        viewModel.removeCart(cartItem.idkeranjangs, cartItem.idproduk)
        refreshCartList()
    }

    var qty: Int = 0
    override fun onDecreaseClicked(cartItem: ProdukCart) {
        qty = cartItem.qty - 1
        if(qty >= 1){
            viewModel.updateQty(qty, cartItem.idkeranjangs, cartItem.idproduk)
        }else if(qty < 1){
            viewModel.removeCart(cartItem.idkeranjangs, cartItem.idproduk)
        }
        refreshCartList()
    }

    override fun onIncreaseClicked(cartItem: ProdukCart) {
        qty = cartItem.qty + 1
        viewModel.updateQty(qty, cartItem.idkeranjangs, cartItem.idproduk)
        refreshCartList()
    }
}