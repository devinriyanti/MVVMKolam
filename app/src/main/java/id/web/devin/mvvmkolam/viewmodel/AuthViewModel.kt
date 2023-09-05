package id.web.devin.mvvmkolam.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import id.web.devin.mvvmkolam.model.Pengguna
import org.json.JSONObject

class AuthViewModel(application: Application):AndroidViewModel(application) {
    val statusLD = MutableLiveData<Boolean?>()

    private val TAG = "volleyTAG"
    private var queue:RequestQueue?= null

    fun loginUser(email: String, password:String) {
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/login.php"

        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener{ response ->
                Log.d("data", response)
                var data = JSONObject(response)
                var status = data.getString("result")
                if(status.equals("success")){
                    statusLD.value = true
                    Log.d("showvolley",status.toString())
                }else{
                    statusLD.value = false
                    Log.d("showError", response.toString())
                }
            },
            Response.ErrorListener { error ->
                // Menangani kesalahan
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("showvolley", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = password
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun registerUser(
        nama: String,
        email: String,
        alamat:String,
        noTelp: String,
        pwd: String,
        role: String,
        idkota:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/registrasi.php"
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
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("showvolley", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["email"] = email
                params["alamat"] = alamat
                params["telepon"] = noTelp
                params["password"] = pwd
                params["role"] = role
                params["idkota"] = idkota
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun registerAdmin(
        nama: String,
        email: String,
        alamat:String,
        norekening:String,
        namarekening:String,
        noTelp: String,
        pwd: String,
        role: String,
        idkota:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/registrasiadmin.php"
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
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("showvolley", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["email"] = email
                params["alamat"] = alamat
                params["norekening"] = norekening
                params["nama_rekening"] = namarekening
                params["telepon"] = noTelp
                params["password"] = pwd
                params["role"] = role
                params["idkota"] = idkota
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