package ru.hspm.myapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.hspm.myapp.data.Plant

interface PerenualApi {
    @GET("species-list")
    suspend fun getPlants(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("q") query: String? = null
    ): PlantResponse
}

data class PlantResponse(
    val data: List<Plant>,
    val total: Int,
    val currentPage: Int,
    val lastPage: Int,
    val perPage: Int
) 