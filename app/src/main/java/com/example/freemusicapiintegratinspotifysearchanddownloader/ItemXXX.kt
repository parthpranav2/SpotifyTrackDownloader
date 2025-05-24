package com.example.freemusicapiintegratinspotifysearchanddownloader

data class ItemXXX(
    val contentRating: ContentRating,
    val cover: List<Cover>,
    val description: String,
    val id: String,
    val name: String,
    val podcast: Podcast,
    val releaseDate: ReleaseDate,
    val type: String,
    val uri: String
)