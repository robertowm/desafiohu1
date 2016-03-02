package com.hotelurbano.desafiohu1.repository.index

import com.google.inject.Inject
import com.hotelurbano.desafiohu1.model.HotelAvailability
import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexWriter
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat

import scala.io.Source

class HotelAvailabilityIndex @Inject()(hotelIndex: HotelIndex) extends LuceneIndex[HotelAvailability] {

  def loadData(writer: IndexWriter) =
    Source
      .fromInputStream(getClass.getResourceAsStream("/artefatos/disp.txt"))
      .getLines
      .foreach { line =>
        val entry = line.split(',') match {
          case Array(hotelId, date, total) =>
            val hotel = hotelIndex.getById(hotelId).get
            new HotelAvailability(hotelId, hotel.city, hotel.name,
              DateTime.parse(date, DateTimeFormat.forPattern("dd/MM/yyyy"))
                .withZoneRetainFields(DateTimeZone.UTC),
              total.toInt)
        }
        writer.addDocument(entry.toDocument)
      }

  protected def convertDocument(document: Document) =
    new HotelAvailability(document)

}
