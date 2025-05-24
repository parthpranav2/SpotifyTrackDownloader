package com.example.freemusicapiintegratinspotifysearchanddownloader

data class Data(
    val albums: Albums,
    val artists: ArtistsX,
    val episodes: Episodes,
    val genres: Genres,
    val playlists: Playlists,
    val podcasts: Podcasts,
    val topResults: TopResults,
    val tracks: Tracks,
    val users: Users
)