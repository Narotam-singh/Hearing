package com.example.hearingapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hearingapp.databinding.ActivityResultDisplayBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.util.*


class ResultDisplay : AppCompatActivity() {
    private lateinit var binding: ActivityResultDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResultDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val points: ArrayList<ArrayList<Double>> = intent.getSerializableExtra("points") as ArrayList<ArrayList<Double>>
        val linelist: ArrayList<Entry> = ArrayList()
        for(i in points)
        {
            val x=i[0].toFloat()
            val y=i[1].toFloat()
            Log.i("points",x.toString())
            Log.i("points",y.toString())
            linelist.add(Entry(x,y))
        }
        val lineDataset=LineDataSet(linelist,"Data")
        val linedata=LineData(lineDataset)
        binding.graph.data=linedata
        lineDataset.color=Color.BLACK
        lineDataset.valueTextColor= Color.BLUE
        lineDataset.valueTextSize=15f

        val xAxis:XAxis=binding.graph.xAxis

        xAxis.setValueFormatter(MyXAxisFormatter())
        lineDataset.valueFormatter=PointValueFormatter()

    }

    class PointValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toString() +" dB"
        }
    }

    class MyXAxisFormatter : IndexAxisValueFormatter() {
        private val frequencies = arrayListOf(125.0, 250.0, 500.0, 1000.0, 2000.0, 4000.0, 8000.0)
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            axis!!.setLabelCount(10,true)
            return (frequencies.getOrNull(value.toInt()) ?: (value.toString() + "Hz").toString()).toString()
        }
    }

}


