package com.heichan.campusnavigator.data.remote

import com.heichan.campusnavigator.data.remote.dto.PlaceDto
import com.heichan.campusnavigator.data.remote.dto.RouteResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/** REST contract for the optional campus backend (see README). */
interface CampusApi {

    @GET("places")
    suspend fun getPlaces(): List<PlaceDto>

    @GET("route")
    suspend fun getRoute(
        @Query("from") from: String,
        @Query("to") to: String,
    ): RouteResponseDto
}

object ApiClient {
    // Point this at your own backend.
    private const val BASE_URL = "https://campus-navigator.example.com/api/"

    private val client = okhttp3.OkHttpClient.Builder()
        .addInterceptor(
            okhttp3.logging.HttpLoggingInterceptor().apply {
                level = okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
            }
        )
        .build()

    val campusApi: CampusApi = retrofit2.Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()
        .create(CampusApi::class.java)
}
