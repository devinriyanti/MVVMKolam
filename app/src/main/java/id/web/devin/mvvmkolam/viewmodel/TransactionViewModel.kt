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
import id.web.devin.mvvmkolam.model.Transaction
import org.json.JSONObject

class TransactionViewModel(application: Application):AndroidViewModel(application) {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null
    val transaksiLD = MutableLiveData<ArrayList<Transaction>>()
    val transaksiDetailLD = MutableLiveData<Transaction>()
    val statusLD = MutableLiveData<Boolean?>()
    val loadingErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private lateinit var result:ArrayList<Transaction>

    fun insertTransaksi(
        ongkir:Int,
        idkeranjang:Int,
        idkolam:String,
        email_pengguna:String,
        idkota:Int,
        alamat:String
    ){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/transaksi.php"
        val stringReq = object :StringRequest(Method.POST, url,
        Response.Listener {response ->
            Log.d("respon", response)
            var data = JSONObject(response)
            var status = data.getString(("result"))
            if(status.equals("success")){
                statusLD.value = true
                Log.d("transaksiVolley",status.toString())
            }else{
                Log.d("transaksiError",response.toString())
            }
        },
        Response.ErrorListener {error->
            Toast.makeText(getApplication(),"Kesalahan Saat Menambahkan Produk", Toast.LENGTH_SHORT).show()
            Log.d("showvolley", error.toString())
        }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["ongkos_kirim"] = ongkir.toString()
                params["id_keranjangs"] = idkeranjang.toString()
                params["idkolam"] = idkolam
                params["email_pengguna"] = email_pengguna
                params["id_kota"] = idkota.toString()
                params["alamat_kirim"] = alamat
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchTransaksiList(email:String, status:String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/transaksilist.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val sType = object : TypeToken<List<Transaction>>(){ }.type
                Log.d("responTransaksi", response)
                if(response.contains("error")){
                    transaksiLD.value = ArrayList()
                }else{
                    result = Gson().fromJson<ArrayList<Transaction>>(response,sType)
                    transaksiLD.value = result
                }
                loadingLD.value = false
            },
            Response.ErrorListener { error ->
                loadingErrorLD.value = true
                Toast.makeText(getApplication(),"Kesalahan Saat Menampilkan Produk", Toast.LENGTH_SHORT).show()
                Log.d("transactionError",error.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["status"] = status
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchTransactionDetail(email:String, status: String, idtransaksi: String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/transaksidetail.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val sType = object : TypeToken<Transaction>(){ }.type
                val resultDetail = Gson().fromJson<Transaction>(response,sType)
                transaksiDetailLD.value = resultDetail
                Log.d("transaksiSuccess",response.toString())
                loadingLD.value = false
            },
            Response.ErrorListener { error ->
                loadingErrorLD.value = true
                Toast.makeText(getApplication(),"Kesalahan Saat Menampilkan Produk", Toast.LENGTH_SHORT).show()
                Log.d("transaksiError",error.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["status"] = status
                params["idtrx"] = idtransaksi
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchTransaksiAdmin(email:String, status:String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/transaksiadmin.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val sType = object : TypeToken<List<Transaction>>(){ }.type
                Log.d("responTransaksi", response)
                if(response.contains("error")){
                    transaksiLD.value = ArrayList()
                }else{
                    result = Gson().fromJson<ArrayList<Transaction>>(response,sType)
                    transaksiLD.value = result
                }
                loadingLD.value = false
            },
            Response.ErrorListener { error ->
                loadingErrorLD.value = true
                Toast.makeText(getApplication(),"Kesalahan Saat Menampilkan Produk", Toast.LENGTH_SHORT).show()
                Log.d("transactionError",error.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["status"] = status
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchTransaksiAdminDetail(email:String, status: String, idtransaksi: String){
        loadingErrorLD.value = false
        loadingLD.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/transaksiadmindetail.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val sType = object : TypeToken<Transaction>(){ }.type
                val resultDetail = Gson().fromJson<Transaction>(response,sType)
                transaksiDetailLD.value = resultDetail
                Log.d("transaksiSuccess",response.toString())
                loadingLD.value = false
            },
            Response.ErrorListener { error ->
                loadingErrorLD.value = true
                Toast.makeText(getApplication(),"Kesalahan Saat Menampilkan Produk", Toast.LENGTH_SHORT).show()
                Log.d("transaksiError",error.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["status"] = status
                params["idtransaksi"] = idtransaksi
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateStatus(idtransaksi: String, status: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/updatetransaksi.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateTransaksi", data.toString())
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
                params["idtransaksi"] = idtransaksi
                params["status"] = status
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun konfirmasiTransaksi(idtransaksi: String, status: String, no_resi:String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/transaksikonfirmasi.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateTransaksi", data.toString())
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
                params["idtransaksi"] = idtransaksi
                params["status"] = status
                params["no_resi"] = no_resi
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateBuktiPembayaran(urlBukti:String, idtransaksi: String){
        loadingLD.value = true
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://lokowai.shop/uploadbuktitrf.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateTransaksi", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    loadingLD.value = false
                    Log.d("showSuccess",it.toString())
                }else{
                    Log.d("showError",it.toString())
                }
            },
            Response.ErrorListener {
                Toast.makeText(getApplication(),"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["idtransaksi"] = idtransaksi
                params["urlBukti"] = urlBukti
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

}