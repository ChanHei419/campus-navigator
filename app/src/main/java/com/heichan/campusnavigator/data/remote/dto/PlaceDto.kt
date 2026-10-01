package com.heichan.campusnavigator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlaceDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)

data class RouteResponseDto(
    @SerializedName("distance_metres") val distanceMetres: Int,
    @SerializedName("path") val path: List<String>,
)
