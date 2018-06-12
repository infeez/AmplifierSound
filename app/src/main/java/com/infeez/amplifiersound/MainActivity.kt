package com.infeez.amplifiersound

import android.content.Context
import android.media.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private val rateHz = 48000

    private lateinit var audioRecord: AudioRecord
    private lateinit var audioTrack: AudioTrack
    private lateinit var audioManager: AudioManager
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        findViewById<Button>(R.id.enable).setOnClickListener {
            volumeControlStream = AudioManager.MODE_IN_COMMUNICATION
            recordAndPlay()
        }

        findViewById<Button>(R.id.exit).setOnClickListener { finish() }
    }

    private fun init() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.STREAM_MUSIC

        val min = AudioRecord.getMinBufferSize(rateHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        audioRecord = AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, rateHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, min * 4)

        val maxJitter = AudioTrack.getMinBufferSize(rateHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)

        audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, rateHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, maxJitter * 4, AudioTrack.MODE_STREAM)
    }

    private fun recordAndPlay() {
        val lin = ShortArray(8192)
        var num: Int
        audioRecord.startRecording()
        audioTrack.play()
        isPlaying = true
        while (isPlaying) {
            num = audioRecord.read(lin, 0, lin.size)
            audioTrack.write(lin, 0, num)
        }
    }
}
