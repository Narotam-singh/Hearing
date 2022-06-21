package com.example.hearingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.hearingapp.databinding.ActivityResultDisplayBinding

class ResultDisplay : AppCompatActivity() {
    private lateinit var binding: ActivityResultDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResultDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val points: HashMap<String,String> = intent.getSerializableExtra("points") as HashMap<String, String>
        binding.tvpoint.text= points.size.toString()

    }
}