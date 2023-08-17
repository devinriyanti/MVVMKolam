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
import com.google.gson.reflect.TypeToken
import id.web.devin.mvvmkolam.model.Pengguna
import org.json.JSONObject

class ProfilViewModel(application: Application):AndroidViewModel(application) {
    val userLD = MutableLiveData<Pengguna>()
    val statusLD = MutableLiveData<Boolean?>()

    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null
    fun fetchProfil(email: String) {
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/profildetail.php"
        val stringReq = object : StringRequest(
            Method.POST, url,
            Response.Listener{ response ->
                val sType = object : TypeToken<Pengguna>() {}.type
                val result = Gson().fromJson<Pengguna>(response, sType)
//                Log.d("showvolley",result.toString())
                userLD.value = result

                Log.d("showvolley",response.toString())
            },
            {
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("showError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateUser(
        email:String,
        nama: String,
        alamat: String,
        noTelp: String,
        gender:String,
        tglLahir:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/editpengguna.php"
        val stringReq = object : StringRequest(
            Method.POST, url,
            Response.Listener{ response ->
                val data = JSONObject(response)
                Log.d("dataProfil", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    statusLD.value = true
                    Log.d("showSuccess",response.toString())
                }else{
                    statusLD.value = false
                    Log.d("showError",response.toString())
                }
            },
            {
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["nama"] = nama
                params["alamat"] = alamat
                params["telepon"] = noTelp
                params["gender"] = gender
                params["tglLahir"] = tglLahir
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