package com.salvaperez.camerarecord

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.salvaperez.camerarecord.commons.utils.Event
import com.salvaperez.camerarecord.commons.utils.Resource
import com.salvaperez.camerarecord.domain.model.VideoModel
import com.salvaperez.camerarecord.domain.usecase.DeleteVideoUseCase
import com.salvaperez.camerarecord.domain.usecase.GetAllVideosUseCase
import com.salvaperez.camerarecord.domain.usecase.GetVideoFileUriUseCase
import com.salvaperez.camerarecord.presentation.main.MainActivityViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getAllVideosUseCase: GetAllVideosUseCase

    @Mock
    lateinit var deleteVideoUseCase: DeleteVideoUseCase

    @Mock
    lateinit var getVideoFilerUri: GetVideoFileUriUseCase

    private lateinit var vm: MainActivityViewModel
    private val getVideos: Observer<Resource<List<VideoModel>>> = mock()
    private val startVideoIntent : Observer<Event<Unit>> = mock()

    private val uiScope = CoroutineScope(Dispatchers.Main)

    @Before
    fun setUp() {
        vm = MainActivityViewModel(getAllVideosUseCase, deleteVideoUseCase, getVideoFilerUri)

    }

    @Test
    fun `observing LiveData start video intent`() {
        vm.startVideoIntent.observeForever(startVideoIntent)
        vm.startVideoIntent()

        verify(startVideoIntent).onChanged(any())
    }

    @Test
    fun `observing LiveData finds the videos`() {
        uiScope.launch {
            val movie = VideoModel("1", "sdfasdf")
            val movieList : ArrayList<VideoModel> = arrayListOf()
            movieList.add(movie)
            whenever(getAllVideosUseCase.invoke()).thenReturn(movieList)

            vm.getVideoList.observeForever(getVideos)

            vm.getVideos()

            assertEquals(vm.startVideoIntent.value, Resource.success(movieList))
        }
    }

    @Test
    fun `observing LiveData delete the videos`() {
        uiScope.launch {
            whenever(deleteVideoUseCase.invoke(1)).thenReturn(emptyList())

            vm.getVideoList.observeForever(getVideos)
            verify(getVideos).onChanged(any())
        }
    }
}