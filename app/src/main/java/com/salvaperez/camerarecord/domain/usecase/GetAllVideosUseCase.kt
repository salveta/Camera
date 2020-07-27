package com.salvaperez.camerarecord.domain.usecase

import com.salvaperez.camerarecord.domain.repository.VideoRepository

class GetAllVideosUseCase(private val videoRepository: VideoRepository){

    suspend operator fun invoke() = videoRepository.getVideos()

}