package com.salvaperez.camerarecord.domain.usecase

import com.salvaperez.camerarecord.domain.repository.VideoRepository

class DeleteVideoUseCase(private val videoRepository: VideoRepository){

    suspend operator fun invoke(
        videoId: Int
    ) = videoRepository.deleteVideo(videoId)

}