package com.example.musicplayer.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.Model.SongModel
import com.example.musicplayer.R
import com.squareup.picasso.Picasso
import java.util.*

class SongsAdapter(private val arrayList: ArrayList<SongModel>, val context:Context) :
    RecyclerView.Adapter<SongsAdapter.MyViewHolder>() {

    class MyViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.album_thumbnail)
        val songName: TextView = itemView.findViewById(R.id.song_name)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val views = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return MyViewHolder(views)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Picasso.get().load(model.image).centerCrop().into(holder.imageView)

        holder.songName.text = arrayList[position].name
        holder.artistName.text = arrayList[position].artist
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}