package com.salvaperez.camerarecord.commons.di

import android.app.Application
import android.content.ContentResolver
import com.salvaperez.camerarecord.data.datasource.VideoDataSource
import com.salvaperez.camerarecord.domain.repository.VideoRepository
import com.salvaperez.camerarecord.domain.usecase.DeleteVideoUseCase
import com.salvaperez.camerarecord.domain.usecase.GetAllVideosUseCase
import com.salvaperez.camerarecord.domain.usecase.GetVideoFileUriUseCase
import com.salvaperez.camerarecord.presentation.main.MainActivity
import com.salvaperez.camerarecord.presentation.main.MainActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(
            listOf(
            videos
        )
        )
    }
}

val videos = module(override = true) {

    single { androidContext().contentResolver as ContentResolver }

    scope(named<MainActivity>()) {
        viewModel {
            MainActivityViewModel(
                get(),
                get(),
                get()
            )
        }
        scoped { GetAllVideosUseCase(get()) }
        scoped { DeleteVideoUseCase(get()) }
        scoped { GetVideoFileUriUseCase(get()) }
    }

    single<VideoRepository> {
        VideoDataSource(get(), get())
    }

}
