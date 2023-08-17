package id.web.devin.mvvmkolam.model

import androidx.room.PrimaryKey
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
    val kolam:String,
    val nama:String?,
    val kota: String?,
    val deskripsi: String?,
    @SerializedName("kuantitas")
    var qty: Int?,
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
    val pelatih:ArrayList<Pelatih>
)

data class Pengguna(
    val email: String?,
    val nama:String?,
    val alamat:String?,
    val telepon:String?,
    val jenis_kelamin:String?,
    @SerializedName("tanggal_lahir")
    val tglLahir: String?,
    @SerializedName("password")
    val pwd:String?,
    val role:Role
)

data class Cart(
    @SerializedName("IdKolam")
    val id:String,
    @SerializedName("namaKolam")
    val nama:String,
    val produk: ArrayList<ProdukCart>
)

data class ProdukCart(
    val idkeranjangs: Int,
    val idkolam:String,
    val total_harga:Double,
    @SerializedName("email_pengguna")
    val email:String,
    val namaProduk:String,
    val harga:Double,
    val qty: Int,
    val gambar:String
)

data class ShippingCostRequest(
    val origin: String,
    val destination: String,
    val weight: Int,
    val key: String,
    val courier:String
)

data class ShippingResponse(
    val rajaongkir: RajaongkirResponse
)

data class RajaongkirResponse(
    val query: Query,
    val status: Status,
    val origin_details: LocationDetails,
    val destination_details: LocationDetails,
    val results: List<ShippingService>
)

data class Query(
    val origin: String,
    val destination: String,
    val weight: Int,
    val key: String,
    val courier: String
)

data class Status(
    val code: Int,
    val description: String
)

data class LocationDetails(
    val city_id: String,
    val province_id: String,
    val province: String,
    val type: String,
    val city_name: String,
    val postal_code: String
)

data class ShippingService(
    val code: String,
    val name: String,
    val costs: List<ShippingCost>
)

data class ShippingCost(
    val service: String,
    val description: String,
    val cost: List<CostDetail>
)

data class CostDetail(
    val value: Int,
    val etd: String,
    val note: String
)

enum class Gender(val displayText:String) {
    Other("--Pilih Jenis Kelamin--"),
    Laki_Laki("Laki-Laki"),
    Perempuan("Perempuan")
}

enum class Role{
    Pengguna,
    Admin
}