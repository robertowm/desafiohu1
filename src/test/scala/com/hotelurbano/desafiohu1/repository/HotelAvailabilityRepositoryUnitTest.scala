package com.hotelurbano.desafiohu1.repository

import com.google.inject.Inject
import com.twitter.inject.Test
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat

class HotelAvailabilityRepositoryUnitTest @Inject()(
                                                     hotelAvailabilityRepository: HotelAvailabilityRepository
                                                   ) extends Test {

  "Search" should {
    "find a room at Mercatto Casa Hotel between 06/05/15 to 08/05/15" in {
      val begin = DateTime.parse("06/05/2015", DateTimeFormat.forPattern("dd/MM/yyyy"))
        .withZoneRetainFields(DateTimeZone.UTC)
      val end   = DateTime.parse("08/05/2015", DateTimeFormat.forPattern("dd/MM/yyyy"))
        .withZoneRetainFields(DateTimeZone.UTC)
      val city = "Araruama"
      val name = "Mercatto Casa Hotel"

      val result = hotelAvailabilityRepository searchAvailableByRangeAndNameAndCity(begin, end, name, city)
      result should have size 1
    }
  }


}
