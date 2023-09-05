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
import id.web.devin.mvvmkolam.model.Provinsi

class ProvinsiListViewModel(application: Application):AndroidViewModel(application) {
    val provinsiLD = MutableLiveData<List<Provinsi>>()
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchProvinsi(){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/kotalist.php"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response->
                val sType = object : TypeToken<List<Provinsi>>(){ }.type
                val result = Gson().fromJson<List<Provinsi>>(response, sType)
                provinsiLD.value = result
                Log.d("showvolley", response.toString())
            },
            {
                Log.d("showvolley", it.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
}