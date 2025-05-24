package com.example.freemusicapiintegratinspotifysearchanddownloader

data class Item(
    val artists: Artists,
    val coverArt: List<CoverArt>,
    val date: Date,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)