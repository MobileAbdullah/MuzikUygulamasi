package com.example.musicstream

import androidx.media3.exoplayer.ExoPlayer
import com.example.musicstream.models.SongModel
import android.content.Context
import androidx.media3.common.MediaItem
import com.google.firebase.firestore.FirebaseFirestore

object MyExoplayer {

    private var exoPlayer : ExoPlayer? = null
    private var currentSong : SongModel? = null


    fun getCurrentSong() : SongModel?{
        return currentSong
    }


    fun getInstance(): ExoPlayer?{
        return exoPlayer
    }

    fun startPlaying(context: Context, song : SongModel){

        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }

        if (currentSong!= song){
            currentSong = song
            updateCount()
            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }


    }

    fun updateCount(){

        currentSong?.id?.let { id ->
            FirebaseFirestore.getInstance().collection("songs")
                .document(id)
                .get().addOnSuccessListener {
                    var latesCount = it.getLong("count")
                    if (latesCount == null){
                        latesCount = 1L
                    }else{
                        latesCount++
                    }

                    FirebaseFirestore.getInstance().collection("songs")
                        .document(id)
                        .update(mapOf("count" to latesCount))
                }

        }

    }
}