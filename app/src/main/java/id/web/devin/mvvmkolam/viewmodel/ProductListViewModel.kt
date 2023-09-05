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
import id.web.devin.mvvmkolam.model.Produk
import org.json.JSONObject

class ProductListViewModel(application: Application):AndroidViewModel(application) {
    val produkLD = MutableLiveData<Produk>()
    val statusLD = MutableLiveData<Boolean?>()
    val statusRemoveLD = MutableLiveData<Boolean?>()

    private val TAG = "volleyTAG"
    private var queue:RequestQueue? = null

    fun refresh(idproduk:String){
        queue = Volley.newRequestQueue(getApplication())
        var url = "https://lokowai.shop/productdetail.php?id=$idproduk"

        val stringReq = StringRequest(Request.Method.GET,url,
            { response->
                val sType = object:TypeToken<Produk>(){ }.type
                val result = Gson().fromJson<Produk>(response,sType)
                produkLD.value = result
                Log.d("showVolley", response.toString())
            },
            {
                Log.d("showVolley", it.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun insertProduct(
        nama: String,
        deskripsi: String,
        qty: Int,
        harga: Double,
        diskon: Double,
        gambar:String,
        berat:Int,
        idkolam:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/insertproduk.php"
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
                params["deskripsi"] = deskripsi
                params["kuantitas"] = qty.toString()
                params["harga"] = harga.toString()
                params["diskon"] = diskon.toString()
                params["gambar"] = gambar
                params["berat"] = berat.toString()
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateProduk(
        nama: String,
        deskripsi: String,
        qty: Int,
        harga: Double,
        diskon: Double,
        berat:Int,
        idproduk:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/editproduk.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("editproduk", data.toString())
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
                params["deskripsi"] = deskripsi
                params["kuantitas"] = qty.toString()
                params["harga"] = harga.toString()
                params["diskon"] = diskon.toString()
                params["berat"] = berat.toString()
                params["idproduk"] = idproduk
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun removeProduk(idproduk:String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/removeproduk.php"
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
                params["idproduk"] = idproduk
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