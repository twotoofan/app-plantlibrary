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
    private val onPlantClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantSearchAdapter.PlantViewHolder>() {

    private var plants: List<Plant> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants.getOrNull(position) ?: return
        holder.bind(plant)
        holder.itemView.setOnClickListener { onPlantClick(plant) }
    }

    override fun getItemCount() = plants.size

    fun updatePlants(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }

    class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.plantCommonName)
        private val scientific: TextView = itemView.findViewById(R.id.plantScientificName)
        private val image: ImageView = itemView.findViewById(R.id.plantImage)

        fun bind(plant: Plant) {
            try {
                name.text = plant.commonName.ifEmpty { 
                    itemView.context.getString(R.string.unknown_plant_name) 
                }
                scientific.text = plant.getDisplayScientificName().ifEmpty { 
                    itemView.context.getString(R.string.scientific_name_not_available)
                }
                
                val imageUrl = plant.getImageUrl()
                if (imageUrl.isNotEmpty()) {
                    image.load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                        error(R.drawable.ic_launcher_background)
                    }
                } else {
                    image.setImageResource(R.drawable.ic_launcher_background)
                }
            } catch (e: Exception) {
                // Fallback to safe defaults if anything fails
                name.text = itemView.context.getString(R.string.error_loading_plant)
                scientific.text = ""
                image.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }
}
