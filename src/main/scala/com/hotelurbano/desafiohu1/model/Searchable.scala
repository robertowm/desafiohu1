package com.hotelurbano.desafiohu1.model

import org.apache.lucene.document.Document

trait Searchable {
  def toDocument: Document
}
