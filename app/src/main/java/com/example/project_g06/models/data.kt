package com.example.project_g06.models

data class data(
    val url: String,
    val fullName: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val addresses: List<addresses>,
    val images: List<images>
) {}