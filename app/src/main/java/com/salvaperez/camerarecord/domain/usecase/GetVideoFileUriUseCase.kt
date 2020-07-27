package com.salvaperez.camerarecord.domain.usecase

import com.salvaperez.camerarecord.domain.repository.VideoRepository

class GetVideoFileUriUseCase(private val videoRepository: VideoRepository){

    fun invoke() = videoRepository.getVideoFileUri()

}