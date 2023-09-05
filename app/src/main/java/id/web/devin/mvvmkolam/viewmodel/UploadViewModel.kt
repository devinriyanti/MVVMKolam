package id.web.devin.mvvmkolam.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import id.web.devin.mvvmkolam.model.UploadResponse
import id.web.devin.mvvmkolam.util.UploadService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class UploadViewModel (application: Application): AndroidViewModel(application){
    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    private val uploadService: UploadService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://lokowai.shop/") // Ganti dengan URL API yang sesuai
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(UploadService::class.java)
    }

    private val _uploadResult = MutableLiveData<Result<UploadResponse>>()
    val uploadResult: LiveData<Result<UploadResponse>> = _uploadResult

    fun uploadImage(imageFile: MultipartBody.Part, folder:RequestBody) {
        val call: Call<UploadResponse> = uploadService.uploadImage(imageFile, folder)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                Log.d("respon", response.toString())
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    Log.d("upload", response.toString())
                    if (uploadResponse != null) {
                        _uploadResult.postValue(Result.Success(uploadResponse))
                    } else {
                        _uploadResult.postValue(Result.Error(Exception("Upload Response Body is null")))
                    }
                } else {
                    _uploadResult.postValue(Result.Error(Exception("Upload Failed")))
                }
            }
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _uploadResult.postValue(Result.Error(t as Exception))
            }
        })
    }
}