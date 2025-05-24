package com.example.freemusicapiintegratinspotifysearchanddownloader

data class Podcast(
    val coverArt: List<CoverArt>,
    val name: String,
    val publisher: Publisher,
    val uri: String
)