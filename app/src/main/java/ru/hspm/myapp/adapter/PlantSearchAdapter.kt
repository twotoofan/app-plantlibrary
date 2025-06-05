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
import com.google.gson.Gson


class PlantSearchAdapter(
    private val onPlantClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantSearchAdapter.PlantViewHolder>() {

    private var plants: List<Plant> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plants[position])
        holder.itemView.setOnClickListener { onPlantClick(plants[position]) }
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
            name.text = plant.commonName ?: "Без названия"
            scientific.text = plant.scientificName?.joinToString(", ") ?: "Научное название отсутствует"
            image.load(plant.defaultImage?.regularUrl)
        }
    }
}
