package com.example.hearingapp

import android.annotation.SuppressLint
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
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResultDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pointsRight: ArrayList<ArrayList<Double>> = intent.getSerializableExtra("pointsRight") as ArrayList<ArrayList<Double>>
        val pointsLeft: ArrayList<ArrayList<Double>> = intent.getSerializableExtra("pointsLeft") as ArrayList<ArrayList<Double>>
        var rightavg=0
        var leftavg=0
        val linelistRight: ArrayList<Entry> = ArrayList()
        val linelistLeft: ArrayList<Entry> = ArrayList()
        for(i in pointsRight)
        {
            val x=i[0].toFloat()
            val y=i[1].toFloat()
            rightavg = (rightavg+y).toInt()
            Log.i("points",x.toString())
            Log.i("points",y.toString())
            linelistRight.add(Entry(x,y))
        }
        rightavg /= pointsRight.size
        for(i in pointsLeft)
        {
            val x=i[0].toFloat()
            val y=i[1].toFloat()
            leftavg= (leftavg+y).toInt()
            Log.i("points",x.toString())
            Log.i("points",y.toString())
            linelistLeft.add(Entry(x,y))
        }
        leftavg/=pointsLeft.size

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
        xAxis.setValueFormatter(MyXAxisFormatter())
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setTextSize(7f)
        xAxis.setTextColor(Color.BLACK)
        xAxis.setDrawAxisLine(true)
        xAxis.setGranularity(0f)
        xAxis.setCenterAxisLabels(true)

        lineDataset[0].valueFormatter=PointValueFormatter()
        lineDataset[1].valueFormatter=PointValueFormatter()

        //Printing Result
        if(leftavg>40)
        {
            binding.leftResult.text="For Left Ear \nWe Recommend You to \nvisit ENT specialist"
//            binding.leftResult.setTextColor(Color.RED)
            binding.cardLeftResult.setBackgroundColor(Color.RED)
        }
        else {
//            binding.leftResult.setTextColor(Color.GREEN)
            binding.cardLeftResult.setBackgroundColor(Color.GREEN)
            binding.leftResult.text = "Left Hearing \nis Normal"
        }
        if(rightavg>40)
        {
//            binding.rightResult.setTextColor(Color.RED)
            binding.cardRightResult.setBackgroundColor(Color.RED)
            binding.rightResult.text="For Right Ear \nWe Recommend You to \nvisit ENT specialist"
        }
        else {
            binding.cardRightResult.setBackgroundColor(Color.GREEN)
//            binding.rightResult.setTextColor(Color.GREEN)
            binding.rightResult.text = "Right Hearing \nis Normal"
        }
    }

    class PointValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return ""
        }
    }

    class MyXAxisFormatter : IndexAxisValueFormatter() {
        private val frequencies : FloatArray = floatArrayOf(125.0f, 500.0f, 2000.0f, 8000.0f)
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            axis!!.setLabelCount(4,true)
            axis.mCenteredEntries=frequencies
            return (frequencies.getOrNull(value.toInt()) ?: (value.toString() + "Hz")).toString()
        }
    }
}


