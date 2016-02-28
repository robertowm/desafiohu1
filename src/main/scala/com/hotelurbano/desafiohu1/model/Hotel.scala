package com.hotelurbano.desafiohu1.model

import org.apache.lucene.document.{TextField, IntField, Field, Document}

case class Hotel(id: Int, city: String, name: String) extends Searchable {
  def this(document: Document) =
    this(document.get("docID").toInt, document.get("city"), document.get("name"))

  def toDocument: Document = {
    val document = new Document

    document.add(new IntField("docID", id, Field.Store.YES))
    document.add(new TextField("city", city, Field.Store.YES))
    document.add(new TextField("name", name, Field.Store.YES))

    document
  }
}
