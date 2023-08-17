package id.web.devin.mvvmkolam.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.RequestQueue
import id.web.devin.mvvmkolam.model.RajaOngkirService
import id.web.devin.mvvmkolam.model.ShippingCost
import id.web.devin.mvvmkolam.model.ShippingCostRequest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShippingViewModel : ViewModel(){
//    private val TAG = "volleyTAG"
//    private var queue: RequestQueue?= null
    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    private val rajaOngkirService = Retrofit.Builder()
        .baseUrl("https://api.rajaongkir.com/starter/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RajaOngkirService::class.java)

    private val _shippingCosts = MutableLiveData<Result<List<ShippingCost>>>()
    val shippingCosts: LiveData<Result<List<ShippingCost>>> = _shippingCosts

    fun fetchShippingCosts(origin: String, destination: String, weight: Int){
        viewModelScope.launch {
            val apiKey = "47fd74c59e0d322b4bddf2d15eb8ef17"
            val request = ShippingCostRequest(origin, destination, weight, apiKey, "jne")
            val response = rajaOngkirService.calculateShippingCosts(request)
            Log.d("responOngkir",response.toString())
            if (response.isSuccessful) {
                val shippingResponse = response.body()
                Log.d("responship",shippingResponse.toString())
//                shippingResponse?.results?.let {
//                    _shippingCosts.value = Result.Success(it)
//                }?: run{
//                    _shippingCosts.value = Result.Error("Tidak ada ongkos kirim tersedia")
//                }
            } else {
                _shippingCosts.value = Result.Error("Kesalahan saat menghitung ongkos kirim")
            }
        }

    }
}