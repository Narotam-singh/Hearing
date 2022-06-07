package com.example.hearingapp

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.hearingapp.databinding.ActivityMainBinding
import kotlin.experimental.and
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val duration: Int = 6 // seconds
    val sampleRate: Int = 12000
    val numSamples: Int = duration * sampleRate
    val sample = DoubleArray(numSamples)
    var start: Double = 125.0
    var end: Double = 8000.0
    var flag: Boolean = true
    var freqOfTone: Double = 125.0 // hz

    val generatedSnd = ByteArray(2 * numSamples)

    val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var minfreq:Double = end

            val thread: Thread = Thread(Runnable() {
            run() {
                while (start <= end) {
                    if (flag) {
                        binding.btnYes.isClickable = false
                        binding.btnNo.isClickable = false
                        var mid: Double = floor((start + end) / 2)
                        flag=false

                        handler.post(Runnable() {
                            binding.tvFreq.text =
                                "Playing sound of freq " + mid.toString()
                            run() {
                                genTone(mid)
                                Log.i("freq",mid.toString())
                                playSound()
                            }
                            Handler().postDelayed({
                                binding.btnYes.isClickable = true
                                binding.btnNo.isClickable = true
                                binding.btnYes.setOnClickListener {
                                    minfreq = mid
                                    end = mid - 10
                                    flag=true
                                }
                                binding.btnNo.setOnClickListener {
                                    start = mid + 10
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
        for (i in 0 until numSamples) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freq))
        }

        var idx: Int = 0
        for (i in sample) {
            // scale to maximum amplitude
            val value = ((i * 32767)).toInt().toShort()
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (value.toByte() and 0x00ff.toByte())
            generatedSnd[idx++] =
                ((value.toByte() and 0xff00.toByte()) / Math.pow(2.0, 8.0)).toInt().toByte()

        }
    }

    private fun playSound()
    {
        val audioTrack = AudioTrack (
            AudioManager.STREAM_MUSIC,
            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, numSamples,
            AudioTrack.MODE_STATIC)
        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
        //flag=true
    }
}