package com.task.msa.domain

import com.task.msa.data.ApiResponse
import com.task.msa.data.Place
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PizzaJuiceApiService {

    @GET("interpreter")
    suspend fun searchPlaces(
        @Query("data") query: String
    ): Response<ApiResponse>

}

interface FoodRepository {
    suspend fun getPizzaPlaces(): List<Place>
    suspend fun getJuicePlaces(): List<Place>
}