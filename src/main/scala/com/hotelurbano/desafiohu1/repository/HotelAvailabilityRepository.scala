package com.hotelurbano.desafiohu1.repository

import com.hotelurbano.desafiohu1.repository.index.HotelAvailabilityIndexInstance
import org.apache.lucene.document.DateTools
import org.apache.lucene.index.Term
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.lucene.search.{BooleanQuery, PhraseQuery, TermRangeQuery}
import org.apache.lucene.util.BytesRef
import org.joda.time.DateTime

object HotelAvailabilityRepositoryInstance extends HotelAvailabilityRepository

class HotelAvailabilityRepository {

  val index = HotelAvailabilityIndexInstance

  private def createRangeQuery(fieldName: String, begin: DateTime, end: DateTime) = {
    val parsedBegin = DateTools.dateToString(begin.toDate, DateTools.Resolution.DAY)
    val parsedEnd = DateTools.dateToString(end.toDate, DateTools.Resolution.DAY)

    new TermRangeQuery(fieldName, new BytesRef(parsedBegin), new BytesRef(parsedEnd), true, true)
  }

  private def createPhraseQuery(fieldName: String, values: Array[String]) = {
    val builder = new PhraseQuery.Builder()
    values.map{ new Term(fieldName,_) }
      .foreach { builder.add(_) }
    builder.build()
  }

  def searchByRangeAndCity(begin: DateTime, end: DateTime, city: String) =
    index.search(
      new BooleanQuery.Builder()
        .add(createRangeQuery("date", begin, end), Occur.MUST)
        .add(createPhraseQuery("city", city.split(" ")), Occur.MUST)
        .build()
    )

  def searchByRangeAndHotel(begin: DateTime, end: DateTime, hotel: String) =
    index.search(
      new BooleanQuery.Builder()
        .add(createRangeQuery("date", begin, end), Occur.MUST)
        .add(createPhraseQuery("name", hotel.split(" ")), Occur.MUST)
        .build()
    )

}
