package com.example.hearingapp

import android.annotation.SuppressLint
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.VolumeShaper
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.hearingapp.databinding.ActivityMainBinding
import kotlin.text.Typography.amp


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val duration: Int = 6 // seconds
    val sampleRate: Int = 12000
    val numSamples: Int = duration * sampleRate
    val sample = DoubleArray(numSamples)
    var start: Double = 125.0
    var end: Double = 8000.0
    var flag: Boolean = true
    val generatedSnd = ShortArray(numSamples)

    val handler: Handler = Handler()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var minfreq: Double = end
        val frequencies = arrayListOf(125.0, 250.0, 500.0, 1000.0, 2000.0, 4000.0, 8000.0)
        val dbs = arrayListOf(10, 20, 40, 60, 80, 100)
        var freqi: Int = 0
        var dbi: Int = 0
        val thread: Thread = Thread(Runnable() {
            run() {
                var mindb: Int = -20
                while (dbi < dbs.size && dbi >= 0 && freqi < frequencies.size && freqi >= 0) {
//                    if(freqi==frequencies.size-1)
//                    {
//                        Log.i("Freqi",freqi.toString())
//                        val intent: Intent=Intent(this,ResultDispaly::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
                    if (flag)
                    {
                        binding.fabYes.isClickable = false
                        binding.fabNo.isClickable = false
                        flag = false
                        if (mindb != -20) {
                            freqi++
                            dbi = 0
                            mindb = -20
                        }
                        if(freqi<frequencies.size){
                        handler.post(Runnable() {
                            binding.tvFreq.text =
                                "Playing sound of freq = " + frequencies[freqi].toString() + "\n  dB = " + dbs[dbi].toString()
                            run() {
                                genTone(frequencies[freqi], dbs[dbi])
                                playSound()
                            }
                            Handler().postDelayed({
                                binding.fabYes.isClickable = true
                                binding.fabNo.isClickable = true
                                binding.tvFreq.text = "Did you hear that?\n  Press Yes or No"
                                binding.fabYes.setOnClickListener {
                                    if (dbi < dbs.size)
                                        mindb = dbs[dbi]
                                    else
                                        mindb = dbs[dbs.size-1]
                                    flag = true
                                }
                                binding.fabNo.setOnClickListener {
                                    dbi++
                                    flag = true
                                }
                            }, 3000)
                        })
                        }else{
                            startActivity(Intent(this,ResultDispaly::class.java))
                            finish()
                        }
                    }
                }
            }
        })
        thread.start()
    }
    private fun genTone(freq: Double, db: Int) {
        for (i in 0 until numSamples) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freq)) // Sine wave
            val amp = Short.MIN_VALUE + 32768 + Math.pow(10.0, db / 20.0).toInt().toShort()
            generatedSnd[i] =
                (sample[i] * amp).toInt().toShort()  // Higher amplitude increases volume
            Log.i("amp", amp.toString())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun playSound() {
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, numSamples,
            AudioTrack.MODE_STATIC
        )
        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        val config: VolumeShaper.Configuration =
            VolumeShaper.Configuration.Builder(VolumeShaper.Configuration.SINE_RAMP)
                .setDuration(3000)
                .setCurve(floatArrayOf(0f, 1f), floatArrayOf(1f, 1f))
                .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR)
                .build()
        val volumeShaper = audioTrack.createVolumeShaper(config)
        volumeShaper.apply(VolumeShaper.Operation.PLAY)
        val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume), 0)
        audioTrack.play()
    }
}