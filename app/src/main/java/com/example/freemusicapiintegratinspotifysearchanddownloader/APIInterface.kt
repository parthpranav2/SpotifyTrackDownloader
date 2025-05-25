package com.example.freemusicapiintegratinspotifysearchanddownloader

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("search")
    fun getTrackByName(
        @Query("q") query: String,
        @Query("type") type : String = "tracks",
        /*@Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("noOfTopResults") noOfTopResults: Int = 5*/
    ) :Call<ApiResponse>
}