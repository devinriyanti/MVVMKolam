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
import id.web.devin.mvvmkolam.model.Pelatih

class PelatihListViewModel(application: Application):AndroidViewModel(application) {
    val pelatihLD = MutableLiveData<List<Pelatih>>()

    private val TAG = "volleyTAG"
    private var queue: RequestQueue? = null

    fun refresh(idKolam:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://devinriyanti.000webhostapp.com/productlist.php?id=$idKolam"

        val stringReq = StringRequest(
            Request.Method.GET,url,
            { response->
                val sType = object: TypeToken<List<Pelatih>>(){ }.type
                val result = Gson().fromJson<List<Pelatih>>(response,sType)
                pelatihLD.value = result
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