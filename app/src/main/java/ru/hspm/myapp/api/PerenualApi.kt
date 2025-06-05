package ru.hspm.myapp.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.hspm.myapp.data.Plant

interface PerenualApi {
    @GET("species-list")
    suspend fun searchPlants(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): PlantResponse

    @GET("species/details/{id}")
    suspend fun getPlantDetails(
        @Path("id") plantId: Int,
        @Query("key") apiKey: String
    ): Plant
}

data class PlantResponse(
    val data: List<Plant>,
    val total: Int,
    val currentPage: Int,
    val lastPage: Int,
    val perPage: Int
) 