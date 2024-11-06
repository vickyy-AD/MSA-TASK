package com.task.msa.data

import com.task.msa.domain.FoodRepository

/***
  There is some issue in Yelp Api So , Here i use Overpass API, for get the Near by Pizza and Juice Shop locations.
  ***/

class PizzaJuiceRepository : FoodRepository {

    override suspend fun getPizzaPlaces(): List<Place> {
        val query = "[out:json];node(around:1000, 40.748817, -73.985428)['amenity'~'restaurant|cafe|fast_food']['name'~'pizza', i];out;"
        val response = RetrofitInstance.googlePlacesApi.searchPlaces(query)

        return if (response.isSuccessful) {
            response.body()?.elements?.map {
                Place(
                    name = it.tags?.name?:""  ,
                    latitude = it.lat,
                    longitude = it.lon,
                    address = "${it.lat}, ${it.lon}"
                )
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun getJuicePlaces(): List<Place> {
        val query = "[out:json];node(around:1000, 40.748817, -73.985428)['amenity'~'restaurant|cafe|fast_food']['name'~'juice', i];out;"
        val response = RetrofitInstance.googlePlacesApi.searchPlaces(query)

        return if (response.isSuccessful) {
            response.body()?.elements?.map {
                Place(
                    name = it.tags?.name ?: "",
                    latitude = it.lat,
                    longitude = it.lon,
                    address = "${it.lat}, ${it.lon}"
                )
            } ?: emptyList()
        } else {
            emptyList()
        }
    }
}
