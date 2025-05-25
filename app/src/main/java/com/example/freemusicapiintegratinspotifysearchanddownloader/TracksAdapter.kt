package com.example.freemusicapiintegratinspotifysearchanddownloader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class TracksAdapter (private val trackList: List<ItemXXXXXXXXX>,
                     private val onGetSourceClicked: (trackUrl: String) -> Unit
):RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {
    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackImage: ShapeableImageView = itemView.findViewById(R.id.imgThumbnail)
        val trackName: TextView = itemView.findViewById(R.id.txttrackName)
        val trackId: TextView = itemView.findViewById(R.id.txttrackId)
        val btnGetSource: View = itemView.findViewById(R.id.btnGetSource)
        val txtDownloadStatus: TextView = itemView.findViewById(R.id.txtDownloadStatus)
        val progressBar: View = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.eachitem, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.trackName.text = track.name
        holder.trackId.text = track.id
        Picasso.get().load(track.albumOfTrack.coverArt[0].url).into(holder.trackImage)


        // Reset UI
        holder.txtDownloadStatus.visibility = View.GONE
        holder.progressBar.visibility = View.GONE
        holder.btnGetSource.isEnabled = true

        holder.btnGetSource.setOnClickListener {
            holder.progressBar.visibility = View.VISIBLE
            holder.txtDownloadStatus.visibility = View.VISIBLE
            holder.txtDownloadStatus.text = "Obtaining Source..."
            holder.btnGetSource.isEnabled = false

            GlobalData.trackParamURL = "https://open.spotify.com/track/"+track.id

            onGetSourceClicked("https://open.spotify.com/track/"+track.id)

            // After download is done (you need a callback or LiveData/Flow),
            // update UI like:
            // holder.progressBar.visibility = View.GONE
            // holder.txtDownloadStatus.text = "Download Complete"
            // holder.btnDownload.text = "Downloaded"
        }
    }
}