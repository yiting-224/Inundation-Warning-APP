package com.example.inundationwarningapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 對應氣象署 JSON 結構
data class RainfallResponse(val records: Records)
data class Records(val Station: List<StationData>)
data class StationData(val StationName: String, val GeoInfo: GeoInfo, val RainfallElement: RainfallElement)
data class GeoInfo(val CountyName: String, val TownName: String)
data class RainfallElement(val Now: Precipitation)
data class Precipitation(val Precipitation: String)

// 定義 API 介面
interface CwaApiService {
    @GET("v1/rest/datastore/O-A0002-001")
    suspend fun getRainfall(
        @Query("Authorization") apiKey: String,
        @Query("format") format: String = "JSON"
    ): RainfallResponse
}

// 建立一個單例物件來管理 Retrofit
object NetworkClient {
    private const val BASE_URL = "https://opendata.cwa.gov.tw/api/"

    val service: CwaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CwaApiService::class.java)
    }
}