package com.medioambiente.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.models.Video

class VideosAdapter(
    private val videos: List<Video>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.videoThumbnailImageView)
        val title: TextView = itemView.findViewById(R.id.videoTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.title.text = video.titulo


        Glide.with(holder.itemView.context)
            .load(video.urlMiniatura)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.thumbnail)

        holder.itemView.setOnClickListener {
            onItemClick(video.urlVideo)
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }
}