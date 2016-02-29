package com.hotelurbano.desafiohu1.controller

import com.google.inject.Inject
import com.hotelurbano.desafiohu1.repository.{HotelAvailabilityRepository, HotelRepository}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class WidgetController @Inject()(
                                  hotelRepository: HotelRepository,
                                  hotelAvailabilityRepository: HotelAvailabilityRepository
                                ) extends Controller {

  get("/widget") { request: Request =>
    response.ok.view("search.mustache", null)
  }

  get("/widget/autocomplete") { request: Request =>
    val query = request.getParam("query")
    val cities = hotelRepository.searchByCity(query)
      .map { _.city }
      .distinct

    val hotels = hotelRepository.searchByCityOrName(query)
      .map { h => h.name + " - " + h.city }

    cities ++ hotels
  }

  post("/search") { request: Request =>
    val where = request.getParam("where")
    val begin = DateTime.parse(request.getParam("begin"),  DateTimeFormat.forPattern("dd/MM/yyyy"))
    val end = DateTime.parse(request.getParam("end"),  DateTimeFormat.forPattern("dd/MM/yyyy"))
    val responseType = request.getParam("responseType", "json")

    val result = where.split(" - ") match {
      case Array(city) =>
        hotelAvailabilityRepository.searchAvailableByRangeAndCity(begin, end, city)
      case Array(name , city) =>
        hotelAvailabilityRepository.searchAvailableByRangeAndNameAndCity(begin, end, name, city)
    }

    if (responseType.equals("view")) {
      response.ok.view("result.mustache", result)
    } else {
      result
    }
  }

}
