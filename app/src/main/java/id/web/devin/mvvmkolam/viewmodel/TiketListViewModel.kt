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

class TiketListViewModel(application: Application):AndroidViewModel(application) {
    val produkLD = MutableLiveData<List<Produk>>()
    val loadingErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    private val TAG = "volleyTAG"
    private var queue: RequestQueue? = null

    fun refresh(idKolam:String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        var url = "https://devinriyanti.000webhostapp.com/productlist.php?id=$idKolam"

        val stringReq = StringRequest(
            Request.Method.GET,url,
            { response->
                val sType = object: TypeToken<List<Produk>>(){ }.type
                val result = Gson().fromJson<List<Produk>>(response,sType)
                produkLD.value = result

                loadingLD.value = false
                Log.d("showVolley", response.toString())
            },
            {
                loadingErrorLD.value = true
                loadingLD.value = false
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