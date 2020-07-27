package com.salvaperez.camerarecord.presentation.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salvaperez.camerarecord.domain.model.VideoModel
import com.salvaperez.camerarecord.domain.usecase.DeleteVideoUseCase
import com.salvaperez.camerarecord.domain.usecase.GetAllVideosUseCase
import com.salvaperez.camerarecord.domain.usecase.GetVideoFileUriUseCase
import com.salvaperez.camerarecord.commons.utils.Event
import com.salvaperez.camerarecord.commons.utils.Resource
import kotlinx.coroutines.launch

class MainActivityViewModel(private var getAllVideosUseCase: GetAllVideosUseCase,
                            private var deleteVideoUseCase: DeleteVideoUseCase,
                            private var getVideoFileUriUseCase: GetVideoFileUriUseCase) : ViewModel(){

    private val _getVideoList = MutableLiveData<Resource<List<VideoModel>>>()
    val getVideoList: LiveData<Resource<List<VideoModel>>> get() = _getVideoList
    private val _startVideoIntent = MutableLiveData<Event<Unit>>()
    val startVideoIntent: LiveData<Event<Unit>> get() = _startVideoIntent

    fun getVideos(){
        viewModelScope.launch {
            val videoList = getAllVideosUseCase.invoke()

            if(videoList.isNotEmpty()){
                _getVideoList.value = Resource.success(videoList)
            }else{
                _getVideoList.value = Resource.error()
            }
        }
    }

    fun deleteVideo(videoModel: VideoModel){
        viewModelScope.launch {
            val videoList = deleteVideoUseCase.invoke(videoId = videoModel.id.toInt())

            _getVideoList.value = Resource.success(videoList)

            if(videoList.isEmpty()){
                _getVideoList.value = Resource.error()
            }
        }
    }

    fun getVideoFileUri(): Uri = getVideoFileUriUseCase.invoke()

    fun startVideoIntent(){
        _startVideoIntent.value = Event(Unit)
    }
}