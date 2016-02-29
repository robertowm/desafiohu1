package com.hotelurbano.desafiohu1.repository

import com.google.inject.Inject
import com.hotelurbano.desafiohu1.repository.index.HotelAvailabilityIndex
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.lucene.search._
import org.joda.time.DateTime

class HotelAvailabilityRepository @Inject()(index: HotelAvailabilityIndex) {

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
