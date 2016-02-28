package com.hotelurbano.desafiohu1.controller

import com.hotelurbano.desafiohu1.repository.{HotelAvailabilityRepositoryInstance, HotelRepositoryInstance}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class WidgetController extends Controller {

  val hotelRepository = HotelRepositoryInstance
  val hotelAvailabilityRepository = HotelAvailabilityRepositoryInstance

  get("/widget") { request: Request =>
    response.ok.view("search.mustache", null)
  }

  case class AutocompleteResponse(name: String, relatedField: String)

  get("/widget/autocomplete") { request: Request =>
    val query = request.getParam("query")
    val cities = hotelRepository.searchByCity(query)
      .map { _.city }
      .distinct
      .map { AutocompleteResponse(_, "city") }
    val hotels = hotelRepository.searchByCityOrName(query)
      .map { hotel =>
        AutocompleteResponse(hotel.name + " - " + hotel.city, "hotel")
      }

    cities ++ hotels
  }

  post("/search") { request: Request =>

    request.params.foreach {
      _ match {
        case (k, v) => println(k + ":" + v)
      }
    }

    val where = request.getParam("where")
    val begin = DateTime.parse(request.getParam("begin"),  DateTimeFormat.forPattern("dd/MM/yyyy"))
    val end =   DateTime.parse(request.getParam("end"),  DateTimeFormat.forPattern("dd/MM/yyyy"))

    if (request.containsParam("city")) {
      hotelAvailabilityRepository.searchByRangeAndCity(begin, end, request.getParam("city"))
    } else {
      hotelAvailabilityRepository.searchByRangeAndHotel(begin, end, request.getParam("hotel"))
    }
  }



}
