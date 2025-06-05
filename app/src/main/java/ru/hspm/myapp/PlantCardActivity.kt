package ru.hspm.myapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
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
        
        // Get plant ID either from plant object or direct ID
        val plantId = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("plant", Plant::class.java)?.id
        } else {
            @Suppress("DEPRECATION")
            (intent.getSerializableExtra("plant") as? Plant)?.id
        } ?: intent.getIntExtra("plant_id", -1)

        if (plantId != -1) {
            loadPlantData(plantId)
            
            // Show basic info while loading if we have a plant object
            val plant = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("plant", Plant::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("plant") as? Plant
            }
            plant?.let { displayBasicInfo(it) }
        } else {
            showError(getString(R.string.error_invalid_plant))
            finish()
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

        // Setup back button
        findViewById<View>(R.id.button_go_back_to_main2)?.setOnClickListener {
            finish()
        }
    }

    private fun displayBasicInfo(plant: Plant) {
        try {
            plantCommonName.text = plant.commonName
            plantScientificName.text = plant.getDisplayScientificName()
            
            val imageUrl = plant.getImageUrl()
            if (imageUrl.isNotEmpty()) {
                plantImage.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_background)
                }
            } else {
                plantImage.setImageResource(R.drawable.ic_launcher_background)
            }
        } catch (e: Exception) {
            // Ignore errors in basic info display as we'll load full details anyway
        }
    }

    private fun loadPlantData(plantId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NetworkModule.perenualApi.getPlantDetails(
                    apiKey = "sk-66R1684169549aba710861",
                    plantId = plantId
                )
                
                withContext(Dispatchers.Main) {
                    displayPlantData(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError(getString(R.string.error_loading_plant_details, e.message))
                }
            }
        }
    }

    private fun displayPlantData(plant: Plant) {
        try {
            // Update all information with full details
            plantCommonName.text = plant.commonName
            plantScientificName.text = plant.getDisplayScientificName()
            
            // Update care information
            wateringInfo.text = getString(R.string.watering_format, plant.watering.ifEmpty { getString(R.string.info_not_available) })
            sunlightInfo.text = getString(R.string.sunlight_format, plant.getDisplaySunlight().ifEmpty { getString(R.string.info_not_available) })
            cycleInfo.text = getString(R.string.cycle_format, plant.cycle.ifEmpty { getString(R.string.info_not_available) })
            
            val imageUrl = plant.getImageUrl()
            if (imageUrl.isNotEmpty()) {
                plantImage.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_background)
                }
            } else {
                plantImage.setImageResource(R.drawable.ic_launcher_background)
            }
            
            // Hide disease card if no disease info is available
            findViewById<View>(R.id.diseaseCard)?.visibility = View.GONE
        } catch (e: Exception) {
            showError(getString(R.string.error_displaying_plant_details))
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
} 