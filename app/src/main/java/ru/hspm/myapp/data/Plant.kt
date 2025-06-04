package ru.hspm.myapp.data

import com.google.gson.annotations.SerializedName

data class Plant(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("common_name")
    val commonName: String?,
    @SerializedName("scientific_name")
    val scientificName: List<String>?,
    @SerializedName("watering")
    val watering: String?,
    @SerializedName("sunlight")
    val sunlight: List<String>?,
    @SerializedName("cycle")
    val cycle: String?,
    @SerializedName("default_image")
    val defaultImage: DefaultImage?
)

data class DefaultImage(
    @SerializedName("regular_url")
    val regularUrl: String?
)
