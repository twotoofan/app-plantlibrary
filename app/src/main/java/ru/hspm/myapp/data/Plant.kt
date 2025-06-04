package ru.hspm.myapp.data

data class Plant(
    val id: Int,
    val commonName: String,
    val scientificName: List<String>,
    val defaultImage: PlantImage?,
    val watering: String,
    val sunlight: List<String>,
    val cycle: String
)

data class PlantImage(
    val originalUrl: String,
    val regularUrl: String,
    val mediumUrl: String,
    val smallUrl: String,
    val thumbnail: String
) 