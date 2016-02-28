package com.hotelurbano.desafiohu1.repository.index

import com.hotelurbano.desafiohu1.model.Hotel
import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexWriter

import scala.io.Source

class HotelIndex extends LuceneIndex[Hotel] {

  def loadData(writer: IndexWriter) = {
    Source
      .fromFile("artefatos/hoteis.txt")
      .getLines
      .map { line =>
        line.split(',') match {
          case Array(id, city, name) =>
            new Hotel(id, city, name trim)
        }
      }
      .foreach { hotel =>
        writer.addDocument (hotel toDocument)
      }
  }

  protected def convertDocument(document: Document) =
    new Hotel(document)

}
