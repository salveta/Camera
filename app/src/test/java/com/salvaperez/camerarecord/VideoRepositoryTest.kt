package com.salvaperez.camerarecord

import android.app.Application
import android.content.ContentResolver
import android.provider.MediaStore
import com.nhaarman.mockitokotlin2.*
import com.salvaperez.camerarecord.data.datasource.VideoDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.fakes.RoboCursor

@RunWith(MockitoJUnitRunner::class)
class VideoRepositoryTest {

    private lateinit var phoneNumbersRoboCursor: RoboCursor
    private lateinit var contentResolver: ContentResolver

    @Before
    fun setUp() {
        phoneNumbersRoboCursor = RoboCursor()

        contentResolver = mock {
            on {
                query(
                    same(MediaStore.Video.Media.EXTERNAL_CONTENT_URI),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
            } doReturn phoneNumbersRoboCursor
        }

        phoneNumbersRoboCursor.setColumnNames(VIDEO_COLUMNS)
    }

    private val VIDEO_1 = arrayOf(
        "1",
        "VIDEO_1113343431"
    )

    @Test
    fun `get videos return emptyList`() {
        runBlocking {
            val video = VideoDataSource(Application(), contentResolver).getVideos()
            Assert.assertEquals(video.size, 0)
        }
    }

    @Test
    fun `get videos list`() {
        runBlocking {
            phoneNumbersRoboCursor.setResults(
                arrayOf(
                    VIDEO_1
                )
            )

            val video = VideoDataSource(Application(), contentResolver).getVideos()
            Assert.assertEquals(video.size, 1)
        }
    }

    @Test
    fun `delete video`() {
        runBlocking {
            val deleteVideo = VideoDataSource(Application(), contentResolver).deleteVideo(1)
            Assert.assertEquals(deleteVideo.size, 0)
        }
    }

    companion object MockData {
        private val VIDEO_COLUMNS = listOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME
        )
    }

}