package com.salvaperez.camerarecord.presentation.video_play

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.salvaperez.camerarecord.R
import kotlinx.android.synthetic.main.activity_video_play.*

class VideoPlayActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        setVideo(intent.getStringExtra(VIDEO_DATA))
    }

    private fun setVideo(id: String?){
        val uri =  Uri.parse("content://media/external_primary/video/media/$id")

        videoView.setVideoURI(uri)
        videoView.start()
    }

    companion object{
        const val VIDEO_DATA = "video.data"
    }

}