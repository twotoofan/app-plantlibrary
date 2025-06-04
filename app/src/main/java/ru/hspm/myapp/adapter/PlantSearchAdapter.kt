package ru.hspm.myapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.hspm.myapp.R
import ru.hspm.myapp.data.Plant

class PlantSearchAdapter(
    private var plants: List<Plant> = emptyList(),
    private val onPlantClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantSearchAdapter.PlantViewHolder>() {

    class PlantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantImage: ImageView = view.findViewById(R.id.plantImage)
        val plantName: TextView = view.findViewById(R.id.plantName)
        val plantDescription: TextView = view.findViewById(R.id.plantDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        
        holder.plantName.text = plant.commonName
        holder.plantDescription.text = (plant.scientificName ?: "Научное название отсутствует").toString()
        
        // Load plant image using Coil
        holder.plantImage.load(plant.defaultImage?.originalUrl) {
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
            crossfade(true)
        }

        holder.itemView.setOnClickListener { onPlantClick(plant) }
    }

    override fun getItemCount() = plants.size

    fun updatePlants(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
} 