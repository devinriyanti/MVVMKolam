package id.web.devin.mvvmkolam.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentCheckoutBinding
import id.web.devin.mvvmkolam.viewmodel.ShippingViewModel

class CheckoutFragment : Fragment() {
    private lateinit var b:FragmentCheckoutBinding
    private lateinit var viewModel: ShippingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentCheckoutBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShippingViewModel::class.java)

        val origin = "501"
        val destination = "114"
        viewModel.fetchShippingCosts(origin,destination,1000)

        observeView()
    }

    private fun observeView() {
        viewModel.shippingCosts.observe(viewLifecycleOwner, Observer {result->
            when(result){
                is ShippingViewModel.Result.Success->{
                    Log.d("Hasil CO",result.data.toString())
//                    b.txtTotalPengirimanCO.text = result.data.toString()
                }
                is ShippingViewModel.Result.Error->{
                    Log.d("eror",result.message)
                }
            }
        })
    }
}