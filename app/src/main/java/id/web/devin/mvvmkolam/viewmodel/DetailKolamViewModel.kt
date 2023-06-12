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

class DetailKolamViewModel(application: Application):AndroidViewModel(application) {
    val kolamLD = MutableLiveData<Kolam>()

    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchData(idKolam:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://devinriyanti.000webhostapp.com/kolamdetail.php?id=$idKolam"
        val stringReq = StringRequest(Request.Method.GET,url,
            {
                val sType = object :TypeToken<Kolam>(){}.type
                val result = Gson().fromJson<Kolam>(it,sType)
                kolamLD.value = result
            },
            {
                Log.d("showvolley",it.toString())
            }
        ).apply {
            tag = "TAG"
        }
        queue?.add(stringReq)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}