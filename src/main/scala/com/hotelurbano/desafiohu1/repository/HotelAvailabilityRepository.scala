package com.hotelurbano.desafiohu1.repository

import com.google.inject.Inject
import com.hotelurbano.desafiohu1.model.{AvailablePackage, HotelAvailability}
import com.hotelurbano.desafiohu1.repository.index.HotelAvailabilityIndex
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.lucene.search._
import org.joda.time.{Days, DateTime}

class HotelAvailabilityRepository @Inject()(index: HotelAvailabilityIndex) {

  private def filterAvailableInRange(list: List[HotelAvailability],
                                     begin: DateTime, end: DateTime): List[AvailablePackage] = {
    val days = Days.daysBetween(begin.toLocalDate, end.toLocalDate).getDays
    list.groupBy(_.hotelId)
      .filter { _ match {
        case (hotelId, elements) => elements.size >= days
        case _ => false
      }}
      .map( _ match {
        case (hotelId, elements) =>
          val first = elements.head
          val roomsAvailable = elements.map(_.total).min
          new AvailablePackage(hotelId, first.name, first.city, begin, end, roomsAvailable)
      })
      .toList
  }

  def searchAvailableByRangeAndNameAndCity(begin: DateTime, end: DateTime, name: String, city: String) = {
    val list = searchByRangeAndNameAndCity(begin, end, name, city)
    filterAvailableInRange(list, begin, end)
  }

  def searchAvailableByRangeAndCity(begin: DateTime, end: DateTime, city: String) = {
    val list = searchByRangeAndCity(begin, end, city)
    filterAvailableInRange(list, begin, end)
  }

  def searchByRangeAndCity(begin: DateTime, end: DateTime, city: String) =
    index.search(
      new BooleanQuery.Builder()
        .add(index.createRangeQuery("date", begin, end), Occur.MUST)
        .add(index.createNumericRangeQuery("total", 1), Occur.MUST)
        .add(index.createFieldQuery("city", city), Occur.MUST)
        .build(),
      Int.MaxValue)

  def searchByRangeAndNameAndCity(begin: DateTime, end: DateTime, name: String, city: String) =
    index.search(
      new BooleanQuery.Builder()
        .add(index.createRangeQuery("date", begin, end), Occur.MUST)
        .add(index.createNumericRangeQuery("total", 1), Occur.MUST)
        .add(index.createFieldQuery("name", name), Occur.MUST)
        .add(index.createFieldQuery("city", city), Occur.MUST)
        .build,
      Int.MaxValue)

}
