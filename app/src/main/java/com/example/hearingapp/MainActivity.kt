package com.example.hearingapp

import android.media.AudioDeviceInfo
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.VolumeShaper
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
    //var freqOfTone: Double = 125.0 // hz

    val generatedSnd = ShortArray( numSamples)

    val handler: Handler = Handler()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var minfreq:Double = end
        val frequencies = arrayListOf(125.0,250.0,500.0,1000.0,2000.0,4000.0,8000.0)
        var i : Int = frequencies.size-5
            val thread: Thread = Thread(Runnable() {
            run() {
                while (i>=0) {
                    if (flag) {
                        binding.fabYes.isClickable = false
                        binding.fabNo.isClickable = false
                        //var mid: Double = floor((start + end) / 2)
                        flag=false

                        handler.post(Runnable() {
                            binding.tvFreq.text =
                                "Playing sound of freq " + frequencies[i].toString()
                            run() {
                                genTone(frequencies[i])
                                Log.i("freq",frequencies[i].toString())
                                playSound()
                            }
                            Handler().postDelayed({
                                binding.fabYes.isClickable = true
                                binding.fabNo.isClickable = true
                                binding.tvFreq.text="Did you hear that?\n  Press Yes or No"
                                binding.fabYes.setOnClickListener {
                                    flag=true
                                }
                                binding.fabNo.setOnClickListener {
                                    flag=true
                                }
                            }, 3000)


                        })
                    }

                }
            }
        })
        thread.start()
        }



//    override fun onResume() {
//        super.onResume()
//
//        // Use a new tread as this can take a while
//        val thread: Thread = Thread(Runnable() {
//            run() {
//                genTone()
//                handler.post(Runnable() {
//
//                    run() {
//                        playSound()
//                    }
//                })
//            }
//        });
//        thread.start();
//    }

    private fun genTone(freq:Double) {

        for (i in 0 until numSamples) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freq)) // Sine wave
            val amp=Short.MIN_VALUE+32768+Math.pow(10.0, 10/20.0).toInt().toShort()
            generatedSnd[i] = (sample[i] * amp).toInt().toShort()  // Higher amplitude increases volume
            Log.i("amp",amp.toString())
        }



//        var idx: Int = 0
//        for (i in sample) {
//            // scale to maximum amplitude
//            val value = ((i * Short.MAX_VALUE)).toInt().toShort()
////            Log.i("freq", Short.MAX_VALUE.toString())
//            // in 16 bit wav PCM, first byte is the low order byte
////            generatedSnd[idx++] = (value.toByte() and 0x00ff.toByte())
////            generatedSnd[idx++] =
////                ((value.toByte() and 0xff00.toByte()) / Math.pow(2.0, 8.0)).toInt().toByte()
//
//
//        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun playSound()
    {
        val audioTrack = AudioTrack (
            AudioManager.STREAM_MUSIC,
            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC)
        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        val config: VolumeShaper.Configuration = VolumeShaper.Configuration.Builder(VolumeShaper.Configuration.SINE_RAMP)
            .setDuration(6000)
            .setCurve(floatArrayOf(0f, 1f), floatArrayOf(1f, 1f))
            .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR)
            .build()
        val volumeShaper=audioTrack.createVolumeShaper(config)
        volumeShaper.apply(VolumeShaper.Operation.PLAY)
//        Log.i("freq", volumeShaper.volume.toString())
////        audioTrack.write(generatedSnd, 0, generatedSnd.size)
//        val audioManager:AudioManager= getSystemService(AUDIO_SERVICE) as AudioManager
//        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume),0)
////        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION)
////        audioManager.stopBluetoothSco()
////        audioManager.setBluetoothScoOn(false)
////        audioManager.setSpeakerphoneOn(false)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val sound=audioManager.getStreamVolumeDb(AudioManager.STREAM_MUSIC,audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)
//            Log.i("db", sound.toString() + " " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toString() + " " + maxVolume.toString())
//
//        }
        audioTrack.play()

        //flag=true
    }
}