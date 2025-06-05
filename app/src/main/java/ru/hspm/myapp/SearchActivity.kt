package ru.hspm.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hspm.myapp.adapter.PlantSearchAdapter
import ru.hspm.myapp.api.NetworkModule
import ru.hspm.myapp.data.Plant

class SearchActivity : AppCompatActivity() {
    private lateinit var searchInput: TextInputEditText
    private lateinit var adapter: PlantSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        setupViews()
        setupSearchButton()
    }

    private fun setupViews() {
        searchInput = findViewById(R.id.searchInput)
        
        // Setup RecyclerView
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.searchResults)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlantSearchAdapter { plant: Plant ->
            val intent = Intent(this, PlantCardActivity::class.java)
            intent.putExtra("Plant_id", plant.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    private fun setupSearchButton() {
        findViewById<View>(R.id.searchButton).setOnClickListener {
            val query = searchInput.text?.toString()
            if (!query.isNullOrBlank()) {
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NetworkModule.perenualApi.getPlants(
                    apiKey = "sk-lmtr68402c6b1d2e810843",
                    query = query
                )
                
                withContext(Dispatchers.Main) {
                    adapter.updatePlants(response.data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SearchActivity,
                        "Ошибка поиска: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun goBackToMain(view: View) {
        finish()
    }
}
