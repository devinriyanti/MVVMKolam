package id.web.devin.mvvmkolam.model

import com.android.volley.Response
import id.web.devin.mvvmkolam.viewmodel.UploadViewModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Multipart
    @POST("image/bukti")
    fun uploadImage(@Part image: MultipartBody.Part): Call<UploadResponse>
}