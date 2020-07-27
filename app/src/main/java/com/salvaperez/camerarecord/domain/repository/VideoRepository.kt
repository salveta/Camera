package com.salvaperez.camerarecord.domain.repository

import android.net.Uri
import com.salvaperez.camerarecord.domain.model.VideoModel

interface VideoRepository {

    suspend fun getVideos(): List<VideoModel>

    suspend fun deleteVideo(videoId: Int): List<VideoModel>

    fun getVideoFileUri(): Uri

}