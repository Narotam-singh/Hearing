package com.example.hearingapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.hearingapp.databinding.ActivityResultDisplayBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.security.KeyStore

class ResultDisplay : AppCompatActivity() {
    private lateinit var binding: ActivityResultDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResultDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val points: HashMap<String,String> = intent.getSerializableExtra("points") as HashMap<String, String>
        val linelist: ArrayList<Entry> = ArrayList()
        for(i in points)
        {
            val x=i.key.toFloat()
            val y=i.value.toFloat()
            linelist.add(Entry(x,y))
        }
        val lineDataset=LineDataSet(linelist,"Count")
        val linedata=LineData(lineDataset)
        binding.graph.data=linedata
        lineDataset.color=Color.BLACK
        lineDataset.valueTextColor= Color.BLUE
        lineDataset.valueTextSize=13f
    }
}