package com.hotelurbano.desafiohu1.repository

import com.google.inject.Inject
import com.hotelurbano.desafiohu1.repository.index.HotelIndex

class HotelRepository @Inject()(index: HotelIndex) {

  def searchByCity(city: String) =
    index.search("city", city, 5)

  def searchByCityOrName(name: String) =
    index.search(Array("city", "name"), name, 5)

}
