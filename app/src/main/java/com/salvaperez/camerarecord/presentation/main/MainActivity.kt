package com.salvaperez.camerarecord.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.salvaperez.camerarecord.R
import com.salvaperez.camerarecord.domain.model.VideoModel
import com.salvaperez.camerarecord.presentation.main.adapter.VideoAdapter
import com.salvaperez.camerarecord.commons.extensions.gone
import com.salvaperez.camerarecord.commons.extensions.open
import com.salvaperez.camerarecord.commons.extensions.visible
import com.salvaperez.camerarecord.commons.utils.EventObserver
import com.salvaperez.camerarecord.commons.utils.Resource
import com.salvaperez.camerarecord.presentation.video_play.VideoPlayActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

class MainActivity : AppCompatActivity() {

    private val vModel: MainActivityViewModel by currentScope.viewModel(this)
    private var videoAdapter: VideoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listeners()
        initViews()

        vModel.getVideos()
    }

    private fun listeners(){
        fabStartCamera.setOnClickListener { _ ->
            requestPermissions()
        }

        vModel.getVideoList.observe(this, Observer { videoModelList ->
            when(videoModelList.status){
                Resource.Status.SUCCESS -> showVideos(videoModelList.data as List<VideoModel>)
                Resource.Status.ERROR -> showEmptyScreen()
            }
        })

        vModel.startVideoIntent.observe(this, EventObserver {
            dispatchTakeVideoIntent()
        })
    }

    private fun showVideos(videoList: List<VideoModel>) {
        txtEmptyScreen.gone()
        videoAdapter?.videoList = videoList
    }

    private fun showEmptyScreen(){
        txtEmptyScreen.visible()
    }

    private fun initViews(){
        videoAdapter = VideoAdapter(
            onClickVideo = {video -> onClickVideo(video) },
            onClickToDelete = { video -> deleteItem(video) })

        rvVideo.layoutManager = LinearLayoutManager(this)
        rvVideo.adapter = videoAdapter
    }

    private fun onClickVideo(video: VideoModel){
        val extras = Bundle()
        extras.putString(VideoPlayActivity.VIDEO_DATA, video.id)
        open(VideoPlayActivity::class.java, extras)
    }

    private fun deleteItem(videoModel: VideoModel){
        vModel.deleteVideo(videoModel)
    }

    private fun requestPermissions(){
        if (allPermissionsGranted()) {
            vModel.startVideoIntent()
        } else {
            ActivityCompat.requestPermissions(this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                vModel.startVideoIntent()
            } else {
                Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            vModel.getVideos()
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takePictureIntent ->
            val photoURI = vModel.getVideoFileUri()
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent,
                    REQUEST_VIDEO_CAPTURE
                )
            }
        }
    }

    companion object{
        const val REQUEST_VIDEO_CAPTURE = 1
        const val REQUEST_CODE_PERMISSIONS = 10
    }
}