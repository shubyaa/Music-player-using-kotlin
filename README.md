# Music Player Using Kotlin

![Logo](https://github.com/shubyaa/Music-player-using-kotlin/blob/master/music_icon.png)

This is an Android Application made using Kotlin Language. This app is made in **Android 6.0 version** with minimum SDK Version as 23 **(API Level 23)**.

The flow of project goes as follows:-

1) Requests Permissions
2) Displays list of all songs available in the storage.
3) On selecting a song, music player apperars and starts playing.
4) Functions like play, pause, next, previous, shuffle, repeat, share & back can be performed.
5) On back pressed, music stops playing, and can exit the app.

## Request Permissions

We can request permissions using [Activity Compact](https://developer.android.com/reference/androidx/core/app/ActivityCompat#requestPermissions(android.app.Activity,%20java.lang.String[],%20int)).

But first, we will check whether the permission is already granted or not. To check this, we will add **hasPermission()** function which returns Boolean values:-

```kotlin
private fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }
```

 With the help of this, we add request permission :-

```kotlin
private fun requestPermission() {
        val permission = mutableListOf<String>()

        if (!hasPermission()) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permission.toTypedArray(), 8)
        }
    }
```

## Displays list of all songs available in the storage

To display the list inside [ListActivity.kt](https://github.com/shubyaa/Music-player-using-kotlin/blob/master/app/src/main/java/com/example/musicplayer/ListActivity.kt), we will first add [Recycler View](https://developer.android.com/jetpack/androidx/releases/recyclerview) to our layout file i.e. [activity_list.xml](https://github.com/shubyaa/Music-player-using-kotlin/blob/master/app/src/main/res/layout/activity_list.xml).

```kotlin
<androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/white"
        android:nestedScrollingEnabled="false"
        android:orientation="vertical" />
```

Now to add all songs, we need to imagine it like an object first. Thus, keeing this in mind, we will make a model of song containing following parameters.

* image
* name
* artist
* data
* duration
* songUri

Below is the example of SongModel class:-

```kotlin
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
```

To get all songs or any type of media from storage, we need to get the **URI**( Uniform Resource Identifier) of that file/ media. You can learn more about URI [here](https://developer.android.com/reference/android/net/Uri).

Along with these functions, we can also add the Album Art and display it in th list as well as in Player using [Glide](https://bumptech.github.io/glide/).

To add songs inside the recycler View, we need [Adapters](https://developer.android.com/reference/android/widget/Adapter) and [View Holders](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder) so that we can inflate the cards of songs inside the list and display them.

To see the correct implementation of the recyclerView, watch this [video](https://www.youtube.com/watch?v=XgzuQUjjH4M&t=1s).

## On selecting a song, music player apperars and starts playing

On selection of any song present in the list, it should open the player and play the song. To perform this we set the listener in the adapter passing the context of the ListActivity itself making it more stable and effective.

```kotlin
holder.itemView.setOnClickListener {

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)


        }
```

To open the player activity (In our case, MainActivity.kt), we made the use of [Intent](https://developer.android.com/reference/android/content/Intent).

## Functions like play, pause, next, previous, shuffle, repeat, share & back can be performed

On opening of the Player, it looks something like this.

<img src="https://github.com/shubyaa/Music-player-using-kotlin/blob/master/output/Screenshot_1.jpg" alt="SreenShot" width="200"/> <img src="https://github.com/shubyaa/Music-player-using-kotlin/blob/master/output/Screenshot_2.jpg" alt="SreenShot" width="200"/>


Various functions like play, pause, next, previous, shuffle, repeat, share & back can be performed. To see in detail, check out the [ListActivity.kt](https://github.com/shubyaa/Music-player-using-kotlin/blob/master/app/src/main/java/com/example/musicplayer/ListActivity.kt) here.


## On back pressed, music stops playing, and can exit the app

As soon as you return to the list of songs, player stops to play the music & it becomes ready to play the next song.

> It is important to note that when you want to stop a song, we should not only stop the mediaplayer, but also release it.

Example:-

```kotlin
val mediaplayer = MediaPlayer()
...
...
...
 override fun onBackPressed() {
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        mediaPlayer!!.release()
    }
```

## Getting Help

To report a specific problem or feature request, open [a new issue on Github](https://github.com/shubyaa/Music-player-using-kotlin/issues/new).

## License

To know more, check out the [License file](https://github.com/shubyaa/Music-player-using-kotlin/blob/master/LICENSE).

## Download & Install

To use this this applicatioon, download & install the [apk file](https://github.com/shubyaa/Music-player-using-kotlin/blob/master/output/app-debug.apk)
