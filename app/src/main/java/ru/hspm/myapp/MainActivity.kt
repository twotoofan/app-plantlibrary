package ru.hspm.myapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.hspm.myapp.service.PlantNotificationService

class MainActivity : AppCompatActivity() {
    private var isNotificationServiceRunning = false
    private lateinit var notificationToggleButton: ImageButton

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startNotificationService()
        } else {
            Toast.makeText(
                this,
                "Разрешение на уведомления необходимо для работы напоминаний",
                Toast.LENGTH_LONG
            ).show()
            updateNotificationButtonState(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNotificationButton()
        
        // Проверяем разрешения при запуске, но не запускаем сервис автоматически
        checkNotificationPermission()
    }

    private fun setupNotificationButton() {
        notificationToggleButton = findViewById(R.id.notificationToggleButton)
        notificationToggleButton.setOnClickListener {
            if (isNotificationServiceRunning) {
                stopNotificationService()
            } else {
                checkNotificationPermissionAndStart()
            }
        }
        // Устанавливаем начальное состояние кнопки
        updateNotificationButtonState(false)
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Разрешение уже есть
                    updateNotificationButtonState(false)
                }
                else -> {
                    // Запрашиваем разрешение
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun checkNotificationPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startNotificationService()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            startNotificationService()
        }
    }

    private fun startNotificationService() {
        try {
            if (!isNotificationServiceRunning) {
                val serviceIntent = Intent(this, PlantNotificationService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }
                isNotificationServiceRunning = true
                updateNotificationButtonState(true)
                Toast.makeText(this, R.string.notifications_started, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка запуска уведомлений: ${e.message}", Toast.LENGTH_LONG).show()
            updateNotificationButtonState(false)
        }
    }

    private fun stopNotificationService() {
        try {
            if (isNotificationServiceRunning) {
                stopService(Intent(this, PlantNotificationService::class.java))
                isNotificationServiceRunning = false
                updateNotificationButtonState(false)
                Toast.makeText(this, R.string.notifications_stopped, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка остановки уведомлений: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateNotificationButtonState(isEnabled: Boolean) {
        isNotificationServiceRunning = isEnabled
        notificationToggleButton.setImageResource(
            if (isEnabled) R.drawable.ic_notifications_active
            else R.drawable.ic_notifications_off
        )
    }

    fun openLibrary(view: View) {
        val intent = Intent(this, Library::class.java)
        startActivity(intent)
    }

    fun openSearch(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Останавливаем сервис при закрытии приложения
        if (isNotificationServiceRunning) {
            stopNotificationService()
        }
    }
}