package com.example.foodcaloriechecker

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.foodcaloriechecker.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var foodAnalyzer: FoodAnalyzer

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) takePicture.launch(null)
            else binding.tvResult.text = "Нужно разрешение камеры"
        }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { onPhotoReceived(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodAnalyzer = FoodAnalyzer(this)

        binding.btnCapture.setOnClickListener { checkCameraAndTakePhoto() }
    }

    private fun checkCameraAndTakePhoto() {
        val hasPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) takePicture.launch(null)
        else requestCameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun onPhotoReceived(bitmap: Bitmap) {
        binding.imagePreview.setImageBitmap(bitmap)
        binding.tvResult.text = "Анализ..."

        CoroutineScope(Dispatchers.IO).launch {
            val result = foodAnalyzer.analyze(bitmap)
            withContext(Dispatchers.Main) {
                binding.tvResult.text = result?.let {
                    "Продукт: ${it.foodName}
Вес: ${it.estimatedWeightGrams} г
Калории: ${it.estimatedCalories} ккал"
                } ?: "Не удалось распознать еду"
            }
        }
    }
}
