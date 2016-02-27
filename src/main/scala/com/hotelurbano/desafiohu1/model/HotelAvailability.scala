package com.hotelurbano.desafiohu1.model

import com.twitter.finatra.conversions.time.RichStringTime
import org.apache.lucene.document._
import org.joda.time.DateTime

case class HotelAvailability(hotelId: Int, city: String, name: String, date: DateTime, total: Int) {
  def this(document: Document) =
    this(document.get("hotelId").toInt, document.get("city"), document.get("name"),
      document.get("date").toDateTime, document.get("total").toInt)

  def asDocument = {
    val document = new Document
    val parsedDate = DateTools.dateToString(date.toDate, DateTools.Resolution.DAY)

    document.add(new IntField("hotelId", hotelId, Field.Store.YES))
    document.add(new TextField("city", city, Field.Store.YES))
    document.add(new TextField("name", name, Field.Store.YES))
    document.add(new StringField("date", parsedDate, Field.Store.YES))
    document.add(new IntField("total", total, Field.Store.YES))

    document
  }
}
