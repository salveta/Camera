package com.salvaperez.camerarecord.presentation.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salvaperez.camerarecord.R
import com.salvaperez.camerarecord.domain.model.VideoModel
import com.salvaperez.camerarecord.commons.extensions.basicDiffUtil
import com.salvaperez.camerarecord.commons.extensions.inflate
import kotlinx.android.synthetic.main.item_video.view.*

class VideoAdapter(private val onClickVideo: (VideoModel) -> Unit, private val onClickToDelete: (VideoModel) -> Unit): RecyclerView.Adapter<VideoAdapter.ViewHolder>(){

    var videoList: List<VideoModel> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new ->
            old.name == new.name }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_video, false)
        return ViewHolder(view, onClickVideo, onClickToDelete)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoModel = videoList[position]
        holder.bind(videoModel)
    }

    class ViewHolder(view: View, val onClickVideo: (VideoModel) -> Unit, val onClickToDelete: (VideoModel) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(videoItem: VideoModel) {
            itemView.txtVideoName.text = videoItem.name

            itemView.cnstVideoItem.setOnClickListener {
                onClickVideo(videoItem)
            }

            itemView.imvDeleteVideo.setOnClickListener {
                onClickToDelete(videoItem)
            }
        }
    }
}