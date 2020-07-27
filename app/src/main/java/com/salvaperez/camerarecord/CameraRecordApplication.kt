package com.salvaperez.camerarecord

import android.app.Application
import com.salvaperez.camerarecord.commons.di.initDI

class CameraRecordApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}