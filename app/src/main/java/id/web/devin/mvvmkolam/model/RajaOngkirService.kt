package id.web.devin.mvvmkolam.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RajaOngkirService {
    @POST("cost")
    suspend fun calculateShippingCosts(@Body request: ShippingCostRequest): Response<ShippingResponse>
}