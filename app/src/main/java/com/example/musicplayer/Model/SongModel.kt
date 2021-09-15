package com.example.musicplayer.Model

import android.net.Uri


data class SongModel(
    var image: Uri,
    var name: String,
    var artist: String,
    var data: String,
    var duration: Double,
    var songUri: Uri
) {
    //A constructor is made with selective parameters so that whenever we need can make an
    // object of the song even if we have very few parameters.
    constructor(image: Uri, name: String, artist: String, duration: Double, songUri: Uri) : this(
        image = image,
        name = name,
        artist = artist,
        data = "",
        duration = duration,
        songUri = songUri
    ) { }
}