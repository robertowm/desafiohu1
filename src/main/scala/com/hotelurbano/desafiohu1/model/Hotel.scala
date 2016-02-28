package com.hotelurbano.desafiohu1.model

import org.apache.lucene.document._

case class Hotel(id: String, city: String, name: String) extends Searchable {
  def this(document: Document) =
    this(document.get("id"), document.get("city"), document.get("name"))

  def toDocument: Document = {
    val document = new Document

    document.add(new StringField("id", id, Field.Store.YES))
    document.add(new TextField("city", city, Field.Store.YES))
    document.add(new TextField("name", name, Field.Store.YES))

    document
  }
}
