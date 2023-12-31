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
import id.web.devin.mvvmkolam.model.Kolam
import id.web.devin.mvvmkolam.model.Produk

class DetailKolamViewModel(application: Application):AndroidViewModel(application) {
    val kolamLD = MutableLiveData<Kolam>()
    val loadingErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchData(idKolam:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/kolamdetail.php?id=$idKolam"
        val stringReq = StringRequest(Request.Method.GET,url,
            {
                val sType = object :TypeToken<Kolam>(){}.type
                val result = Gson().fromJson<Kolam>(it,sType)
                kolamLD.value = result

                loadingLD.value = false
                loadingErrorLD.value = false
            },
            {
                loadingErrorLD.value = true
                loadingLD.value = false
                Log.d("showvolley",it.toString())
            }
        ).apply {
            tag = "TAG"
        }
        queue?.add(stringReq)
    }

    fun getID(){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/idkolam.php"

        val stringReq = StringRequest(Request.Method.GET,url,
            { response->
                val sType = object:TypeToken<Kolam>(){ }.type
                val result = Gson().fromJson<Kolam>(response,sType)
                kolamLD.value = result
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