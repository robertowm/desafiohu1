package com.hotelurbano.desafiohu1.repository

import com.google.inject.Inject
import com.hotelurbano.desafiohu1.repository.index.HotelAvailabilityIndex
import org.apache.lucene.index.Term
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.lucene.search._
import org.apache.lucene.util.BytesRef
import org.joda.time.DateTime

class HotelAvailabilityRepository @Inject()(index: HotelAvailabilityIndex){

  private def createRangeQuery(fieldName: String, begin: DateTime, end: DateTime) : Query =
    createRangeQuery(fieldName, begin.toString, end.toString)

  private def createRangeQuery(fieldName: String, begin: String, end: String) : Query =
    new TermRangeQuery(fieldName, new BytesRef(end), new BytesRef(end), true, true)

  private def createNumericRangeQuery(fieldName: String, begin: Int, end: Int = Int.MaxValue) : Query =
    NumericRangeQuery.newIntRange(fieldName, begin, end, true, true)

  private def createPhraseQuery(fieldName: String, values: Array[String]) : Query = {
    val builder = new PhraseQuery.Builder()
    values.map{ new Term(fieldName,_) }
      .foreach { builder.add(_) }
    builder.build()
  }

  def searchByRangeAndCity(begin: DateTime, end: DateTime, city: String) =
    index.search(
      new BooleanQuery.Builder()
        .add(createRangeQuery("date", begin, end), Occur.MUST)
        .add(createNumericRangeQuery("total", 1), Occur.MUST)
        .add(createPhraseQuery("city", city.split(" ")), Occur.MUST)
        .build()
    )

  def searchByRangeAndNameAndCity(begin: DateTime, end: DateTime, name: String, city: String) =
    index.search(
      new BooleanQuery.Builder()
        .add(createRangeQuery("date", begin, end), Occur.MUST)
        .add(createNumericRangeQuery("total", 1), Occur.MUST)
        .add(createPhraseQuery("name", name.split(" ")), Occur.MUST)
        .add(createPhraseQuery("city", city.split(" ")), Occur.MUST)
        .build
    )
}
