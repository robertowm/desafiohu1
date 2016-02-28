package com.hotelurbano.desafiohu1.repository.index

import com.hotelurbano.desafiohu1.model.{HotelAvailability, Hotel}
import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source

object HotelAvailabilityIndexInstance extends HotelAvailabilityIndex

class HotelAvailabilityIndex extends LuceneIndex[HotelAvailability] {

  def loadData(writer: IndexWriter) = {
    val hotelIndex = HotelIndexInstance
    Source
      .fromFile("artefatos/disp.txt")
      .getLines
      .foreach { line =>
        val entry = line.split(',') match {
          case Array(hotelId, date, total) =>
            val hotel = hotelIndex.getById(hotelId).get
            new HotelAvailability(hotelId, hotel.city, hotel.name,
                DateTime.parse(date, DateTimeFormat.forPattern("dd/MM/yyyy")), total.toInt)
        }
        writer.addDocument(entry.toDocument)
      }
  }

  protected def convertDocument(document: Document) =
    new HotelAvailability(document)

}
