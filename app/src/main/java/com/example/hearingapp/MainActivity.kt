package com.example.hearingapp

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlin.experimental.and

class MainActivity : AppCompatActivity() {
    val duration: Int = 10 // seconds
    val sampleRate: Int = 10000
    val numSamples: Int = duration * sampleRate
    val sample = DoubleArray(numSamples)

    //private final double sample[] = new double[numSamples];
    val freqOfTone: Double = 125.0 // hz

    val generatedSnd = ByteArray(2 * numSamples)

    val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()

        // Use a new tread as this can take a while
        val thread: Thread = Thread(Runnable() {
            run() {
                genTone()
                handler.post(Runnable() {

                    run() {
                        playSound();
                    }
                })
            }
        });
        thread.start();
    }

    private fun genTone() {
        // fill out the array
        for (i in 0 until numSamples) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone))
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
    }
}