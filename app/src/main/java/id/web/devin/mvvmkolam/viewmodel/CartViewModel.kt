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
import id.web.devin.mvvmkolam.model.Cart
import id.web.devin.mvvmkolam.model.Kolam
import id.web.devin.mvvmkolam.model.Produk
import org.json.JSONObject

class CartViewModel(application: Application):AndroidViewModel(application){
    val statusRemoveLD = MutableLiveData<Boolean?>()
    val statusLD = MutableLiveData<Boolean?>()
    val cartLD = MutableLiveData<ArrayList<Cart>>()
    val cartDetailLD = MutableLiveData<Cart>()
    val loadingErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private lateinit var result:ArrayList<Cart>
    private val TAG = "volleyTAG"
    private var queue:RequestQueue?= null

    fun addToCart(
        idKolam: String,
        total_harga: Double,
        email: String,
        idproduk: String,
        qty: Int,
        harga: Double,
        diskon: Double,
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/addtocart.php"
        val stringReq = object : StringRequest(Method.POST,url,
        Response.Listener { response ->
            Log.d("respon", response)
            var data = JSONObject(response)
            var status = data.getString(("result"))
            if(status.equals("success")){
                statusLD.value = true
                Log.d("addtocartVolley",status.toString())
            }else{
                Log.d("addtocartError",response.toString())
            }
        },
        Response.ErrorListener { error ->
            Toast.makeText(getApplication(),"Kesalahan Saat Menambahkan Produk", Toast.LENGTH_SHORT).show()
            Log.d("showvolley", error.toString())
        }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["idkolam"] = idKolam
                params["total"] = total_harga.toString()
                params["email"] = email
                params["idproduk"] = idproduk
                params["qty"] = qty.toString()
                params["harga"] = harga.toString()
                params["diskon"] = diskon.toString()
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchCartList(email:String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/cartlist.php"
        val stringReq = object : StringRequest(Method.POST, url,
        Response.Listener { response ->
            val sType = object : TypeToken<List<Cart>>(){ }.type
            Log.d("responCart", response)
            if(response.contains("error")){
                cartLD.value = ArrayList()
            }else{
                result = Gson().fromJson<ArrayList<Cart>>(response,sType)
                cartLD.value = result
            }
            loadingLD.value = false
        },
        Response.ErrorListener { error ->
            loadingErrorLD.value = true
            Toast.makeText(getApplication(),"Kesalahan Saat Menampilkan Produk", Toast.LENGTH_SHORT).show()
            Log.d("cartError",error.toString())
        }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchCartDetail(email:String, idKolam: String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/cartdetail.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val sType = object : TypeToken<Cart>(){ }.type
                val resultDetail = Gson().fromJson<Cart>(response,sType)
                cartDetailLD.value = resultDetail

                loadingLD.value = false
            },
            Response.ErrorListener { error ->
                loadingErrorLD.value = true
                Toast.makeText(getApplication(),"Kesalahan Saat Menampilkan Produk", Toast.LENGTH_SHORT).show()
                Log.d("cartError",error.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["idkolam"] = idKolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateQty(qty: Int, idkeranjang: Int, idproduk: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/updatecart.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateCart", data.toString())
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
                params["qty"] = qty.toString()
                params["idkeranjang"] = idkeranjang.toString()
                params["idproduk"] = idproduk
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun removeCart(idkeranjang:Int, idproduk: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/removecart.php"
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
                params["id"] = idkeranjang.toString()
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