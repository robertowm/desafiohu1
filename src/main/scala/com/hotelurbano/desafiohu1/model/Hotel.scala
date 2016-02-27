package com.hotelurbano.desafiohu1.model

import org.apache.lucene.document.{TextField, IntField, Field, Document}

case class Hotel(id: Int, city: String, name: String) {
  def this(document: Document) =
    this(document.get("id").toInt, document.get("city"), document.get("name"))

  def asDocument = {
    val document = new Document

    document.add(new IntField("id", id, Field.Store.YES))
    document.add(new TextField("city", city, Field.Store.YES))
    document.add(new TextField("name", name, Field.Store.YES))

    document
  }
}
