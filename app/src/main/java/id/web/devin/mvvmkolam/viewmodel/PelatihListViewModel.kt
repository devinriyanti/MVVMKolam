package id.web.devin.mvvmkolam.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.web.devin.mvvmkolam.model.Pelatih
import id.web.devin.mvvmkolam.model.Produk
import org.json.JSONObject

class PelatihListViewModel(application: Application):AndroidViewModel(application) {
    val pelatihLD = MutableLiveData<Pelatih>()
    val statusLD = MutableLiveData<Boolean?>()
    val statusRemoveLD = MutableLiveData<Boolean?>()

    private val TAG = "volleyTAG"
    private var queue: RequestQueue? = null

    fun refresh(idpelatih:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/pelatihdetail.php?id=$idpelatih"

        val stringReq = StringRequest(
            Request.Method.GET,url,
            { response->
                val sType = object: TypeToken<Pelatih>(){ }.type
                val result = Gson().fromJson<Pelatih>(response,sType)
                pelatihLD.value = result
                Log.d("showVolley", response.toString())
            },
            {
                Log.d("showVolley", it.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun insertPelatih(
        nama: String,
        tglLahir: String,
        kontak: String,
        tglKarir: String,
        gambar:String,
        deskripsi:String,
        idkolam:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/insertpelatih.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener{ response ->
                var data = JSONObject(response)
                var status = data.getString("result")
                if(status.equals("success")){
                    statusLD.value = true
                    Log.d("showvolley", response.toString())
                }else{
                    statusLD.value = false
                    Log.d("showError", response.toString())
                }
            },
            Response.ErrorListener { error ->
                // Menangani kesalahan
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("showvolley", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["tanggal_lahir"] = tglLahir
                params["kontak"] = kontak
                params["mulai_karir"] = tglKarir
                params["gambar"] = gambar
                params["deskripsi"] = deskripsi
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updatePelatih(
        nama: String,
        tglLahir: String,
        kontak: String,
        tglKarir: String,
        deskripsi:String,
        idkolam:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/editpelatih.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("editpelatih", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    statusLD.value = true
                    Log.d("showSuccess",it.toString())
                }else{
                    statusLD.value = false
                    Log.d("showError",it.toString())
                }
            },
            Response.ErrorListener {
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["tanggal_lahir"] = tglLahir
                params["kontak"] = kontak
                params["mulai_karir"] = tglKarir
                params["deskripsi"] = deskripsi
                params["idpelatih"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun removeKolam(idpelatih:String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/removepelatih.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                Log.d("tes", it)
                if(it.equals("[]")){
                    statusRemoveLD.value = false
                }else{
                    statusRemoveLD.value = true
                    val data = JSONObject(it)
                    val status = data.getString("result")
                    if(status.equals("success")){
                        Log.d("showSuccess",it.toString())
                    }else{
                        Log.d("showError",it.toString())
                    }
                }
            },
            Response.ErrorListener {
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("removeError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["idpelatih"] = idpelatih
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}