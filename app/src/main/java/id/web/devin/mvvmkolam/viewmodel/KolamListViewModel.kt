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
import id.web.devin.mvvmkolam.model.Kolam
import org.json.JSONArray
import org.json.JSONObject

class KolamListViewModel(application: Application):AndroidViewModel(application) {
    val kolamLD = MutableLiveData<List<Kolam>>()
    val loadingErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    val statusLD = MutableLiveData<Boolean?>()
    val statusRemoveLD = MutableLiveData<Boolean?>()
    private lateinit var result:List<Kolam>

    private val TAG = "volleyTAG"
    private var queue:RequestQueue ?= null

    fun refresh(cari:String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/kolam.php?cari=$cari"

        val stringReq = StringRequest(Request.Method.GET, url,
            { response->
                val sType = object : TypeToken<List<Kolam>>(){ }.type
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

    fun insertKolam(
        nama: String,
        alamat: String,
        deskripsi: String,
        gambar: String,
        lokasi: String,
        email:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/insertkolam.php"
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
                params["alamat"] = alamat
                params["deskripsi"] = deskripsi
                params["gambar"] = gambar
                params["lokasi"] = lokasi
                params["email_pengguna"] = email
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateKolam(
        nama:String,
        alamat:String,
        deskripsi:String,
        lokasi:String,
        idkolam:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/editkolam.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("editkolam", data.toString())
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
                params["alamat"] = alamat
                params["deskripsi"] = deskripsi
                params["lokasi"] = lokasi
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun removeKolam(idkolam:String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/removekolam.php"
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
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateMaintenance(status:Int, idkolam: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/updatestatusmaintenance.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("statusKolam", data.toString())
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
                params["status"] = status.toString()
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
    fun updateStatus(status:Int, idkolam: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/updatestatuskolam.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("statusKolam", data.toString())
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
                params["status"] = status.toString()
                params["idkolam"] = idkolam
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