package com.example.tictactoe.game_logics

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import com.example.tictactoe.R

class Music constructor(private val context: Context) {

    private var mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound)

    init {
        mediaPlayer.isLooping = true
    }

    fun pause() = mediaPlayer.pause()

    fun start() = mediaPlayer.start()

    fun switchPlayingState(view: View) {
        if (mediaPlayer.isPlaying) {
            view.setBackgroundResource(R.drawable.mute)
            mediaPlayer.pause()
        } else {
            view.setBackgroundResource(R.drawable.unmute)
            mediaPlayer.start()
        }
    }

}