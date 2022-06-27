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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import java.util.*
import kotlin.collections.ArrayList


class ResultDisplay : AppCompatActivity() {
    private lateinit var binding: ActivityResultDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResultDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pointsRight :ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>()
        val pointsLeft :ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>()
//        val temp : ArrayList<Double> = ArrayList<Double>()
//        temp.add(125.0)
//        temp.add(20.0)
//        pointsRight.add(temp)
//        temp.clear()
//        temp.add(250.0)
//        temp.add(40.0)
//        pointsRight.add(temp)
//        temp.clear()
//        temp.add(500.0)
//        temp.add(60.0)
//        pointsRight.add(temp)
//        temp.clear()
//        temp.add(1000.0)
//        temp.add(20.0)
//        pointsRight.add(temp)
//        temp.clear()
//        temp.add(2000.0)
//        temp.add(10.0)
//        pointsRight.add(temp)
//        temp.clear()
//        temp.add(4000.0)
//        temp.add(60.0)
//        pointsRight.add(temp)
//        temp.clear()
//        temp.add(8000.0)
//        temp.add(10.0)
//        pointsRight.add(temp)
//        temp.clear()
//
//        temp.add(125.0)
//        temp.add(40.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        temp.add(250.0)
//        temp.add(20.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        temp.add(500.0)
//        temp.add(80.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        temp.add(1000.0)
//        temp.add(60.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        temp.add(2000.0)
//        temp.add(10.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        temp.add(4000.0)
//        temp.add(20.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        temp.add(8000.0)
//        temp.add(40.0)
//        pointsLeft.add(temp)
//        temp.clear()
//        val pointsRight: ArrayList<ArrayList<Double>> = intent.getSerializableExtra("pointsRight") as ArrayList<ArrayList<Double>>
//        val pointsLeft: ArrayList<ArrayList<Double>> = intent.getSerializableExtra("pointsLeft") as ArrayList<ArrayList<Double>>
        val linelistRight: ArrayList<Entry> = ArrayList()
        val linelistLeft: ArrayList<Entry> = ArrayList()
        for(i in pointsRight)
        {
            val x=i[0].toFloat()
            val y=i[1].toFloat()
            Log.i("points",x.toString())
            Log.i("points",y.toString())
            linelistRight.add(Entry(x,y))
        }
        for(i in pointsLeft)
        {
            val x=i[0].toFloat()
            val y=i[1].toFloat()
            Log.i("points",x.toString())
            Log.i("points",y.toString())
            linelistLeft.add(Entry(x,y))
        }

        val lineDatasetRight=LineDataSet(linelistRight,"Right Ear")
        val lineDatasetLeft=LineDataSet(linelistLeft,"Left Ear")
        val lineDataset:ArrayList<LineDataSet> = ArrayList<LineDataSet>()
        lineDataset.add(lineDatasetLeft)
        lineDataset.add(lineDatasetRight)
        val linedata=LineData(lineDataset as List<ILineDataSet>?)
        binding.graph.data=linedata
        lineDataset[0].color=Color.BLUE
        lineDataset[0].valueTextColor= Color.BLACK
        lineDataset[0].valueTextSize=15f

        lineDataset[1].color=Color.GREEN
        lineDataset[1].valueTextColor= Color.BLACK
        lineDataset[1].valueTextSize=15f

        val xAxis:XAxis=binding.graph.xAxis
        val frequencies : FloatArray = floatArrayOf(125.0f, 250.0f, 500.0f, 1000.0f, 2000.0f, 4000.0f, 8000.0f)

        xAxis.setValueFormatter(MyXAxisFormatter())
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setTextSize(7f)
        xAxis.setTextColor(Color.BLACK)
        xAxis.setDrawAxisLine(true)
        xAxis.setGranularity(0f)
        xAxis.setCenterAxisLabels(true)
        xAxis.mCenteredEntries=frequencies

        lineDataset[0].valueFormatter=PointValueFormatter()
        lineDataset[1].valueFormatter=PointValueFormatter()

    }

    class PointValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return ""
        }
    }

    class MyXAxisFormatter : IndexAxisValueFormatter() {
        private val frequencies = arrayListOf(125.0, 250.0, 500.0, 1000.0, 2000.0, 4000.0, 8000.0)
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            axis!!.setLabelCount(10,true)
            return (frequencies.getOrNull(value.toInt()) ?: (value.toString() + "Hz")).toString()
        }
    }
}


