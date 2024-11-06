package com.task.msa.data

data class ApiResponse(
    val elements: List<Element>
)

data class Element(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Tags?
)

data class Tags(
    val name: String?,
    val amenity: String?

)
data class Place(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,

    )
