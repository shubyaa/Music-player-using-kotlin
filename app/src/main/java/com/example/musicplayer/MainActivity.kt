package com.example.musicplayer

import android.database.Cursor
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.musicplayer.Model.SongModel


class MainActivity : AppCompatActivity() {
    var position: Int = -1
    var uri: Uri = Uri.EMPTY
    var list: ArrayList<SongModel> = ArrayList()
    private lateinit var runnable: Runnable
    private lateinit var albumImage: ImageView
    private lateinit var songName: TextView
    private lateinit var artistName: TextView
    private lateinit var back: ImageView
    private var handler = Handler()
    private var mediaPlayer: MediaPlayer? = MediaPlayer()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = getAudioFiles()
        position = intent.getIntExtra("position", -1)


        songName = findViewById(R.id.textView)
        artistName = findViewById(R.id.textView2)
        albumImage = findViewById(R.id.album_art)

        back = findViewById(R.id.back)

        val play_pause = findViewById<ImageButton>(R.id.play_pause)
        val previous = findViewById<ImageView>(R.id.previous)
        val next = findViewById<ImageView>(R.id.next)

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val startTime = findViewById<TextView>(R.id.start_time)
        val endTime = findViewById<TextView>(R.id.end_time)

        uri = list[position].songUri

        setLayout(albumImage, songName, artistName, uri)

        playMedia(uri)

        //play & pause Button
        play_pause.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                play_pause.setBackgroundResource(R.drawable.play)
                mediaPlayer!!.pause()
            } else {
                play_pause.setBackgroundResource(R.drawable.pause)
                mediaPlayer!!.start()
            }
        }
        //next & previous Button
        next.setOnClickListener {
            nextPrevious(name = true)
        }
        previous.setOnClickListener {
            nextPrevious(name = false)
        }

        back.setOnClickListener {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            finish()
        }

        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            startTime.text = timeFormat(mediaPlayer!!.currentPosition)
            endTime.text = timeFormat(mediaPlayer!!.duration)

            handler.postDelayed(runnable, 1000)

        }

        handler.postDelayed(runnable, 1000)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    val x = progress / 1000
                    mediaPlayer!!.seekTo(x)

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.seekTo(seekBar!!.progress)
                    startTime.text = seekBar.progress.toString()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.seekTo(seekBar!!.progress)
                }
            }

        })

    }

    private fun setLayout(image: ImageView, song: TextView, artist: TextView, uri: Uri) {
        val byteArray = getAlbumArt(uri)
        Glide.with(applicationContext).asBitmap().load(byteArray).centerCrop().into(image)

        song.text = list[position].name
        artist.text = list[position].artist
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getAudioFiles(): ArrayList<SongModel> {
        val list: ArrayList<SongModel> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val proj = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA, //For Image
            MediaStore.Audio.Media.ARTIST,
        )

        //Cursor is an interface where with the help of which we can access & write data
        // according to the requirement

        val audioCursor: Cursor? = contentResolver.query(
            uri,
            proj,
            null,
            null,
            null
        )

        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    val songName: String =
                        audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val artistName: String =
                        audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val url: String =
                        audioCursor.getString(audioCursor.getColumnIndex((MediaStore.Audio.Media.DATA)))
                    val duration =
                        audioCursor.getDouble(audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val title =
                        audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))

                    val path = Uri.parse(url)

                    val songModel = SongModel(path, songName, artistName, duration, path)

                    list.add(songModel)


                } while (audioCursor.moveToNext())
            }
        }

        // a List has sortby method in which we can sort a list of object with respect to a
        // parameter of the object, in this case it is sorted w.r.t. name
        list.sortBy { it.name }
        audioCursor?.close()

        return list
    }

    fun timeFormat(duration: Int): String {
        val minutes = (duration % (1000 * 60 * 60) / (1000 * 60))
        val seconds = (duration % (1000 * 60 * 60) % (1000 * 60) / 1000)

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun playMedia(uri: Uri) {
        setLayout(albumImage, songName, artistName, uri)

        try {
            if (mediaPlayer != null) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(this@MainActivity, uri)
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    prepare()
                }
                mediaPlayer!!.start()
            }
        } catch (e: Exception) {
            Log.i("exception_PlayMedia", e.toString())
        }
    }

    private fun getAlbumArt(uri: Uri): ByteArray? {

        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(uri.toString())

        val result: ByteArray? = metadataRetriever.embeddedPicture
        metadataRetriever.release()

        return result
    }

    private fun nextPrevious(name: Boolean) {
        if (name) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()

            position = checkPosition(position, true)
            playMedia(list[position].songUri)
        } else {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()

            position = checkPosition(position, false)
            playMedia(list[position].songUri)
        }
    }

    private fun checkPosition(value: Int, increment: Boolean): Int {
        var return_value = 0

        if (increment) {
            if (value == list.size - 1) {
                return_value = 0
            } else {
                return_value = ++position
            }
        } else {
            if (value == 0) {
                return_value = list.size - 1
            } else {
                return_value = --position
            }
        }

        return return_value
    }

    override fun onBackPressed() {
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        finish()
    }

}
