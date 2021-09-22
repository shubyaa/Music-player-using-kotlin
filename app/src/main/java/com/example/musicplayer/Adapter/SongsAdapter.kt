package com.example.musicplayer.Adapter

import android.content.Context
import android.content.Intent
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
import com.example.musicplayer.MainActivity
import com.example.musicplayer.Model.SongModel
import com.example.musicplayer.R

//Adapter is used to adapt a card assigned to your model object in your list view(in this case RecyclerView)
class SongsAdapter(private val arrayList: ArrayList<SongModel>, private val context: Context?) :
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
        //This method retrieves data and displays inside the view (i.e. Card) while binding
        val byteArray = getAlbumArt(arrayList[position].image)

        //Glide Library has a function to convert byte array and display as Bitmap inside the target or ImageView.
        Glide.with(context!!).asBitmap().load(byteArray).centerCrop()
            .placeholder(R.drawable.music_note).into(holder.imageView)


        holder.songName.text = arrayList[position].name
        holder.artistName.text = arrayList[position].artist

        holder.itemView.setOnClickListener {

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)


        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    //AlbumArt - we use metaDataRetriever to retrieve the Image in ByteArray from
    // Uri provided and returns that ByteArray.
    private fun getAlbumArt(uri: Uri): ByteArray? {

        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(uri.toString())

        val result: ByteArray? = metadataRetriever.embeddedPicture
        metadataRetriever.release()

        return result
    }

}