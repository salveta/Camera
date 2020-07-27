package com.salvaperez.camerarecord.data.datasource

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.salvaperez.camerarecord.BuildConfig
import com.salvaperez.camerarecord.domain.model.VideoModel
import com.salvaperez.camerarecord.domain.repository.VideoRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VideoDataSource(private val application: Application, private val contentResolver: ContentResolver) : VideoRepository {

    @SuppressLint("Recycle")
    override suspend fun getVideos(): List<VideoModel> {
        val selection = MediaStore.Video.Media.DATA + LIKE
        val selectionArgs = arrayOf(SELECTION_ARGS)
        val videoList : ArrayList<VideoModel> = arrayListOf()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME
        )

        try {
            val videoCursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null)

            videoCursor?.let {cursor ->
                for (videoPosition in 0 until cursor.count) {
                    val nameVideo = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    val idVideo = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    cursor.moveToPosition(videoPosition)

                    val video =
                        VideoModel(
                            id = cursor.getString(idVideo),
                            name = cursor.getString(nameVideo)
                        )
                    videoList.add(video)
                }
                cursor.close()
            }

            return videoList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun deleteVideo(videoId: Int): List<VideoModel>  {
        contentResolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI , "$ID ='$videoId'", null)
        return getVideos()
    }

    override fun getVideoFileUri(): Uri {
        return getUri()
    }

    private fun getUri(): Uri {
        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val fileName = "$VIDEO_PATH${timeStamp}$VIDEO_FORMAT_PATH"

        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Video.Media.MIME_TYPE,
                    MIME_TYPE
                )
                put(MediaStore.Video.Media.RELATIVE_PATH,
                    DIRECTORY_ANDROID_Q
                )
            }

            uri = resolver.insert(MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues)
        }

        return uri ?: getUriForPreQ(fileName)
    }

    private fun getUriForPreQ(fileName: String): Uri {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val photoFile = File(dir, "/$DIRECTORY/$fileName")
        if (photoFile.parentFile?.exists() == false) photoFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            application,
            BuildConfig.APPLICATION_ID + PROVIDER_PATH,
            photoFile
        )
    }

    companion object{
        const val DIRECTORY = "APPPPP"
        const val DIRECTORY_ANDROID_Q = "DCIM/APPPPP/"
        const val MIME_TYPE = "video/mp4"
        const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        const val VIDEO_PATH = "VIDEO_"
        const val VIDEO_FORMAT_PATH = ".mp4"
        const val PROVIDER_PATH = ".provider"
        const val SELECTION_ARGS = "%APPPPP%"
        const val LIKE = " like?"
        const val ID = "_ID"
    }
}