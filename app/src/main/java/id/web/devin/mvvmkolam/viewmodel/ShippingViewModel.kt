package id.web.devin.mvvmkolam.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.web.devin.mvvmkolam.model.Produk
import id.web.devin.mvvmkolam.model.ShippingCostRequest
import id.web.devin.mvvmkolam.model.ShippingResponse
import id.web.devin.mvvmkolam.util.RajaOngkirService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShippingViewModel : ViewModel(){
    val loadingLD = MutableLiveData<Boolean>()
    val shippingCosts = MutableLiveData<ShippingResponse>()

    private val rajaOngkirService = Retrofit.Builder()
        .baseUrl("https://api.rajaongkir.com/starter/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RajaOngkirService::class.java)


    fun fetchShippingCosts(origin: String, destination: String, weight: Int){
        loadingLD.value = true
//         val apiKey = "f6c65ee25a36a2f2a7767cc130d4e9a6" //devinriyantii
        val apiKey = "0178ae2a9f1df06a92c967cdd512cede" //vinariyantii
        val request = ShippingCostRequest(origin, destination, weight, apiKey, "jne")
        val response = rajaOngkirService.calculateShippingCosts(request)
        response.enqueue(object : Callback<ShippingResponse> {
            override fun onResponse(call: Call<ShippingResponse>, response: Response<ShippingResponse>) {
                if (response.isSuccessful) {
                    loadingLD.value = false
                    shippingCosts.value = response.body()
                } else {
                    Log.d("errorShippingCost","Tidak ada ongkos kirim tersedia")
                }
            }
            override fun onFailure(call: Call<ShippingResponse>, t: Throwable) {
                Log.d("errorShippingCost","Kesalahan saat menghitung ongkos kirim")
            }
        })
    }
}