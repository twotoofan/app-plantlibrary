package ru.hspm.myapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Plant(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("common_name")
    val commonName: String = "",
    @SerializedName("scientific_name")
    val scientificName: List<String> = emptyList(),
    @SerializedName("watering")
    val watering: String = "",
    @SerializedName("sunlight")
    val sunlight: List<String> = emptyList(),
    @SerializedName("cycle")
    val cycle: String = "",
    @SerializedName("default_image")
    val defaultImage: DefaultImage? = null
) : Serializable {
    fun getDisplayScientificName(): String = scientificName.joinToString(", ")
    fun getDisplaySunlight(): String = sunlight.joinToString(", ")
    fun getImageUrl(): String = defaultImage?.regularUrl ?: ""
}

data class DefaultImage(
    @SerializedName("regular_url")
    val regularUrl: String = ""
) : Serializable
