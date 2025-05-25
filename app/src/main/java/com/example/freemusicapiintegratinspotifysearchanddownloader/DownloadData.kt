package com.example.freemusicapiintegratinspotifysearchanddownloader

data class DownloadData(
    val `data`: DataX,
    val generatedTimeStamp: Long,
    val success: Boolean
)