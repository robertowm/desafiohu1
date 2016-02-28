package com.hotelurbano.desafiohu1.repository

import com.hotelurbano.desafiohu1.repository.index.HotelIndexInstance

object HotelRepositoryInstance extends HotelRepository

class HotelRepository {

  val index = HotelIndexInstance

  def searchByCity(city: String) =
    index.search(city, "city")

  def searchByCityOrName(name: String) =
    index.search(name, Array("city", "name"))

}
