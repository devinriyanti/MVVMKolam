package id.web.devin.mvvmkolam.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.web.devin.mvvmkolam.model.Produk

class ProductListViewModel(application: Application):AndroidViewModel(application) {
    val produkLD = MutableLiveData<Produk>()

    private val TAG = "volleyTAG"
    private var queue:RequestQueue? = null

    fun refresh(idproduk:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://devinriyanti.000webhostapp.com/productdetail.php?id=$idproduk"

        val stringReq = StringRequest(Request.Method.GET,url,
            { response->
                val sType = object:TypeToken<Produk>(){ }.type
                val result = Gson().fromJson<Produk>(response,sType)
                produkLD.value = result
                Log.d("showVolley", response.toString())
            },
            {
                Log.d("showVolley", it.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}