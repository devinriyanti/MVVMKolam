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
import id.web.devin.mvvmkolam.model.ShippingResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShippingViewModel : ViewModel(){
    val loadingLD = MutableLiveData<Boolean>()
    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    private val rajaOngkirService = Retrofit.Builder()
        .baseUrl("https://api.rajaongkir.com/starter/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RajaOngkirService::class.java)

    private val _shippingCosts = MutableLiveData<Result<List<ShippingResponse>>>()
    val shippingCosts: LiveData<Result<List<ShippingResponse>>> = _shippingCosts

    fun fetchShippingCosts(origin: String, destination: String, weight: Int){
        loadingLD.value = true
        viewModelScope.launch {
//            val apiKey = "f6c65ee25a36a2f2a7767cc130d4e9a6" //devinriyantii
            val apiKey = "0178ae2a9f1df06a92c967cdd512cede" //vinariyantii
            val request = ShippingCostRequest(origin, destination, weight, apiKey, "jne")
            val response = rajaOngkirService.calculateShippingCosts(request)
            Log.d("responOngkir",response.toString())
            if (response.isSuccessful) {
                val shippingResponse = response.body()
                Log.d("responship",shippingResponse.toString())
                shippingResponse?.let {
                    _shippingCosts.value = Result.Success(listOf(it))
                    loadingLD.value = false
                }?: run{
                    _shippingCosts.value = Result.Error("Tidak ada ongkos kirim tersedia")
                }
            } else {

                _shippingCosts.value = Result.Error("Kesalahan saat menghitung ongkos kirim")
            }
        }

    }
}