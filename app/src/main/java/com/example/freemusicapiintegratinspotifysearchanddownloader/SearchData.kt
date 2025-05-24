package com.example.freemusicapiintegratinspotifysearchanddownloader

data class SearchData(
    val `data`: Data,
    val generatedTimeStamp: Long,
    val success: Boolean
)