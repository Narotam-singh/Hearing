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
                        binding.btnYes.isClickable = false
                        binding.btnNo.isClickable = false
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
                                binding.btnYes.isClickable = true
                                binding.btnNo.isClickable = true
                                binding.btnYes.setOnClickListener {
//                                    minfreq = mid
//                                    end = mid - 10
//                                    i=i-1
                                    flag=true
                                }
                                binding.btnNo.setOnClickListener {
//                                    start = mid + 10
//                                    Log.i("min freq",frequencies[i].toString())
//                                    i=-1
                                    flag=true
                                }
                            }, 3000)


                        })
                    }

                }
            }
        })
        thread.start()

                //flag=false
            //}
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
        // fill out the array
//        var K =2.0 * Math.PI / (sampleRate)
//        var f : Double=freq
//        var q : Double=0.0
        for (i in 0 until numSamples) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freq)) // Sine wave
            generatedSnd[i] = (sample[i] * (Short.MAX_VALUE - 25000)).toInt().toShort()  // Higher amplitude increases volume
//            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freq))
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
            .setDuration(10000)
            .setCurve(floatArrayOf(0f, 1f), floatArrayOf(1f, 1f))
            .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR)
            .build()
        val volumeShaper=audioTrack.createVolumeShaper(config)
        volumeShaper.apply(VolumeShaper.Operation.PLAY)
        Log.i("freq", volumeShaper.volume.toString())
//        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        val audioManager:AudioManager= getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (0.8*maxVolume).toInt(),0)
//        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION)
//        audioManager.stopBluetoothSco()
//        audioManager.setBluetoothScoOn(false)
//        audioManager.setSpeakerphoneOn(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val sound=audioManager.getStreamVolumeDb(AudioManager.STREAM_MUSIC,audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)
            Log.i("db", sound.toString() + " " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toString() + " " + maxVolume.toString())

        }
        audioTrack.play()

        //flag=true
    }
}