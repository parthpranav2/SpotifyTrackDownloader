package com.example.freemusicapiintegratinspotifysearchanddownloader

data class ItemXXXXXXX(
    val albumOfTrack: AlbumOfTrack,
    val artists: Artists,
    val avatarImg: List<AvatarImg>,
    val contentRating: ContentRating,
    val cover: List<CoverXXX>,
    val description: String,
    val duration: Duration,
    val id: String,
    val name: String,
    val owner: Owner,
    val playability: Playability,
    val profile: ProfileX,
    val type: String,
    val uri: String
)