package ru.hspm.myapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import ru.hspm.myapp.data.Plant

class PlantCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plantcard)

        // Получаем объект Plant из Intent
        val plant = intent.getSerializableExtra("plant") as? Plant

        // Инициализируем view-элементы
        val plantImage = findViewById<ImageView>(R.id.plantImage)
        val plantCommonName = findViewById<TextView>(R.id.plantCommonName)
        val plantScientificName = findViewById<TextView>(R.id.plantScientificName)
        val wateringInfo = findViewById<TextView>(R.id.wateringInfo)
        val sunlightInfo = findViewById<TextView>(R.id.sunlightInfo)
        val cycleInfo = findViewById<TextView>(R.id.cycleInfo)
        //val diseaseInfo = findViewById<TextView>(R.id.diseaseInfo)

        plant?.let {
            // Загрузка изображения
            plantImage.load(it.defaultImage?.regularUrl)

            // Заполнение текстовых полей
            plantCommonName.text = it.commonName ?: "Без названия"
            plantScientificName.text = it.scientificName?.joinToString(", ") ?: "Научное название отсутствует"
            wateringInfo.text = "Полив: ${it.watering ?: "нет данных"}"
            sunlightInfo.text = "Свет: ${it.sunlight?.joinToString(", ") ?: "нет данных"}"
            cycleInfo.text = "Цикл: ${it.cycle ?: "нет данных"}"
            //diseaseInfo.text = it.diseaseInfo ?: ""
        }
    }
}
