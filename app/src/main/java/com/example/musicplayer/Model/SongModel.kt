package com.example.musicplayer.Model


data class SongModel (
    var image: String,
    var name: String,
    var artist: String,
    var data: String,
    var duration: Double
) {
    constructor(image: String, name: String, artist: String, duration: Double): this(
        image = image,
        name = name,
        artist = artist,
        data = "",
        duration = duration
    )  {    }
}