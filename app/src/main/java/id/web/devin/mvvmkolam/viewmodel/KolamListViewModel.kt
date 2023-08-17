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
import org.json.JSONArray
import org.json.JSONObject

class KolamListViewModel(application: Application):AndroidViewModel(application) {
    val kolamLD = MutableLiveData<List<Kolam>>()
    val loadingErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private lateinit var result:List<Kolam>

    private val TAG = "volleyTAG"
    private var queue:RequestQueue ?= null

    fun refresh(){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/kolam.php"

        val stringReq = StringRequest(Request.Method.GET, url,
            { response->
                val sType = object : TypeToken<List<Kolam>>(){ }.type
                val result = Gson().fromJson<List<Kolam>>(response, sType)
                kolamLD.value = result

                loadingLD.value = false
                Log.d("showvolley", response.toString())
            },
            {
                loadingErrorLD.value = false
                Log.d("showvolley", it.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun refreshAdmin(email:String, role:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/kolamadmin.php?email=$email&role=$role"
        val stringReq = StringRequest(Request.Method.GET,url,
            { response->
                val sType = object : TypeToken<List<Kolam>>(){ }.type
                Log.d("response", response)
                if(response.contains("error")){
                    result = emptyList()
                }else{
                    result = Gson().fromJson<List<Kolam>>(response, sType)
                }
                kolamLD.value = result

                loadingLD.value = false
                Log.d("showvolley", response.toString())
            },
            {
                loadingErrorLD.value = true
                Log.d("showerror", it.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}