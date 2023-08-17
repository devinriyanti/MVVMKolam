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
    val statusLD = MutableLiveData<Boolean?>()
    val cartLD = MutableLiveData<ArrayList<Cart>>()
    val produkCartLD = MutableLiveData<Cart>()
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
            if(response.isNullOrEmpty()){
//                result = emp()
            }else{
                result = Gson().fromJson<ArrayList<Cart>>(response,sType)
            }
            cartLD.value = result
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

    fun fetchProductCart(email:String){
        queue = Volley.newRequestQueue(getApplication())
        Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/cartlist.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val sType = object : TypeToken<List<Cart>>(){ }.type
                Log.d("responCart", response)
                if(response.isNullOrEmpty()){
//                    result = emptyList()
                }else{
                    result = Gson().fromJson<ArrayList<Cart>>(response,sType)
                }
                cartLD.value = result
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

    fun updateQty(produk: Produk, qty:Int){

    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }

}