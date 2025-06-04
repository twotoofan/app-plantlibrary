package ru.hspm.myapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.hspm.myapp.api.NetworkModule
import ru.hspm.myapp.data.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantCardActivity : AppCompatActivity() {
    private lateinit var plantImage: ImageView
    private lateinit var plantCommonName: TextView
    private lateinit var plantScientificName: TextView
    private lateinit var wateringInfo: TextView
    private lateinit var sunlightInfo: TextView
    private lateinit var cycleInfo: TextView
    private lateinit var diseaseInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plantcard)

        initViews()
        
        // Get plant ID from intent
        val plantId = intent.getIntExtra("plant_id", -1)
        if (plantId != -1) {
            loadPlantData(plantId)
        }
    }

    private fun initViews() {
        plantImage = findViewById(R.id.plantImage)
        plantCommonName = findViewById(R.id.plantCommonName)
        plantScientificName = findViewById(R.id.plantScientificName)
        wateringInfo = findViewById(R.id.wateringInfo)
        sunlightInfo = findViewById(R.id.sunlightInfo)
        cycleInfo = findViewById(R.id.cycleInfo)
        diseaseInfo = findViewById(R.id.diseaseInfo)
    }

    private fun loadPlantData(plantId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Replace "YOUR_API_KEY" with actual API key
                val response = NetworkModule.perenualApi.getPlants(
                    apiKey = "YOUR_API_KEY",
                    query = plantId.toString()
                )
                
                val plant = response.data.firstOrNull()
                plant?.let { displayPlantData(it) }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }

    private suspend fun displayPlantData(plant: Plant) = withContext(Dispatchers.Main) {
        plantCommonName.text = plant.commonName
        plantScientificName.text = plant.scientificName.joinToString(", ")
        wateringInfo.text = "Watering: ${plant.watering}"
        sunlightInfo.text = "Sunlight: ${plant.sunlight.joinToString(", ")}"
        cycleInfo.text = "Cycle: ${plant.cycle}"
        
//        plant.defaultImage?.let { image ->
//            plantImage.load(image.regularUrl) {
//                crossfade(true)
//                placeholder(R.drawable.ic_launcher_background) // Replace with your placeholder
//                error(R.drawable.ic_launcher_background) // Replace with your error image
//            }
//        }
    }
} 