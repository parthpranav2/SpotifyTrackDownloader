package com.example.freemusicapiintegratinspotifysearchanddownloader

data class AlbumOfTrack(
    val coverArt: List<CoverArt>,
    val id: String,
    val name: String,
    val uri: String
)