package ru.hspm.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hspm.myapp.adapter.PlantSearchAdapter
import ru.hspm.myapp.api.NetworkModule
import ru.hspm.myapp.data.Plant

class SearchActivity : AppCompatActivity() {
    private lateinit var searchInput: TextInputEditText
    private lateinit var adapter: PlantSearchAdapter
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        setupViews()
        setupSearchInput()
    }

    private fun setupViews() {
        searchInput = findViewById(R.id.searchInput)
        
        // Setup RecyclerView
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.searchResults)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = PlantSearchAdapter { plant ->
            navigateToPlantDetails(plant)
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearchInput() {
        // Add text change listener for automatic search
        searchInput.addTextChangedListener { text ->
            searchJob?.cancel()
            if (!text.isNullOrBlank()) {
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500) // Debounce for 500ms
                    performSearch(text.toString())
                }
            } else {
                adapter.updatePlants(emptyList())
            }
        }

        // Setup search button
        findViewById<View>(R.id.searchButton).setOnClickListener {
            val query = searchInput.text?.toString()
            if (!query.isNullOrBlank()) {
                searchJob?.cancel()
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NetworkModule.perenualApi.searchPlants(
                    apiKey = "sk-66R1684169549aba710861",
                    query = query
                )
                
                withContext(Dispatchers.Main) {
                    if (response.data.isEmpty()) {
                        showMessage("No plants found for '$query'")
                    }
                    adapter.updatePlants(response.data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Search failed: ${e.message}")
                }
            }
        }
    }

    private fun navigateToPlantDetails(plant: Plant) {
        val intent = Intent(this, PlantCardActivity::class.java).apply {
            putExtra("plant", plant)
            putExtra("plant_id", plant.id) // Fallback
        }
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun goBackToMain(view: View) {
        finish()
    }
}

