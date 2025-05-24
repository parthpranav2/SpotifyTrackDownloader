package com.example.freemusicapiintegratinspotifysearchanddownloader

data class Featured(
    val cover: List<CoverXXX>,
    val description: String,
    val id: String,
    val name: String,
    val owner: Owner,
    val type: String,
    val uri: String
)