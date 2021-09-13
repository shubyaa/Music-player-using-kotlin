package com.example.musicplayer

import android.database.Cursor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.Adapter.SongsAdapter.*
import com.example.musicplayer.Model.SongModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var position: Int = -1
    var uri: Uri = Uri.EMPTY
    var list: ArrayList<SongModel> = ArrayList()
    private lateinit var runnable: Runnable
    private var handler = Handler()
    private var mediaPlayer: MediaPlayer? = MediaPlayer()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = getAudioFiles()
        position = intent.getIntExtra("position", -1)


        val songName = findViewById<TextView>(R.id.textView)
        val artistName = findViewById<TextView>(R.id.textView2)
        val albumImage = findViewById<ImageView>(R.id.album_art)

        val play_pause = findViewById<ImageButton>(R.id.play_pause)
        val previous = findViewById<ImageView>(R.id.previous)
        val next = findViewById<ImageView>(R.id.next)

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val startTime = findViewById<TextView>(R.id.start_time)
        val endTime = findViewById<TextView>(R.id.end_time)
        getIntentMethod()

        songName.text = list[position].name
        artistName.text = list[position].artist


        play_pause.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                play_pause.setBackgroundResource(R.drawable.play)
                mediaPlayer!!.pause()
            } else {
                play_pause.setBackgroundResource(R.drawable.pause)
                mediaPlayer!!.start()
            }
        }

        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            startTime.text = mediaPlayer!!.currentPosition.toString()
            endTime.text = mediaPlayer!!.duration.toString()

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

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getIntentMethod() {

        uri = Uri.EMPTY
        uri = list[position].songUri

        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()

            if (uri != Uri.EMPTY) {
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
                } else {
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

            }
        } else {
            if (uri != Uri.EMPTY) {

                if (mediaPlayer != null) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.reset()
                    mediaPlayer!!.release()

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
                } else {
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
            }
        }
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


    override fun onBackPressed() {
        mediaPlayer!!.stop()
        finish()

    }
}
