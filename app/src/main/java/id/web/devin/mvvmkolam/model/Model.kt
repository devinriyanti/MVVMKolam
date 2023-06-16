package id.web.devin.mvvmkolam.model

import com.google.gson.annotations.SerializedName

data class Pelatih(
    val id:String?,
    val nama:String?,
    @SerializedName("tanggal_lahir")
    val tglLahir:String?,
    val kontak:String?,
    @SerializedName("mulai_karir")
    val tglKarir:String?,
    val deskripsi:String?,
    @SerializedName("gambar")
    val gambarUrl: String?
)

data class Produk(
    @SerializedName("id")
    val idproduk:String?,
    val idkolam:String?,
    val nama:String?,
    val kota: String?,
    val deskripsi: String?,
    @SerializedName("kuantitas")
    val qty: Double?,
    val harga:Double?,
    val diskon:Double?,
    @SerializedName("gambar")
    val gambarUrl: String?,
    val berat:Double?
)

data class Tiket(
    val idproduk:String?,
    val idkolam:String?,
    val nama:String?,
    val kota: String?,
    val deskripsi: String?,
    @SerializedName("kuantitas")
    val qty: Double?,
    val harga:Double?,
    val diskon:Double?,
    @SerializedName("gambar")
    val gambarUrl: String?,
    val berat:Double?
)

data class Kolam(
    val id: String?,
    val nama: String?,
    val alamat: String?,
    val deskripsi: String?,
    val email: String?,
    @SerializedName("gambar")
    val gambarUrl: String?,
    val is_maintenance: String?,
    val kota: String?,
    @SerializedName("email_pengguna")
    val admin: String?,
    @SerializedName("product")
    val produk:ArrayList<Produk>,
    val tiket:ArrayList<Tiket>,
    val pelatih:ArrayList<Pelatih>,

)

