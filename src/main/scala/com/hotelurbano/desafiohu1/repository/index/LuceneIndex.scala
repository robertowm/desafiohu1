package com.hotelurbano.desafiohu1.repository.index

import com.hotelurbano.desafiohu1.model.Searchable
import org.apache.lucene.analysis.br.BrazilianAnalyzer
import org.apache.lucene.analysis.util.CharArraySet
import org.apache.lucene.document.Document
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig, Term}
import org.apache.lucene.queryparser.classic.{MultiFieldQueryParser, QueryParser}
import org.apache.lucene.search._
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.util.BytesRef
import org.joda.time.DateTime

abstract class LuceneIndex[MODEL <: Searchable] {

  protected def loadData(writer: IndexWriter)

  protected def convertDocument(document: Document): MODEL

  private val analyzer = new BrazilianAnalyzer(new CharArraySet(0, true))

  private val directory: RAMDirectory = new RAMDirectory

  private val indexWriter: IndexWriter = {
    val writer = new IndexWriter(directory, new IndexWriterConfig(analyzer))
    loadData(writer)
    writer.close
    writer
  }

  private val indexSearcher: IndexSearcher =
    new IndexSearcher(DirectoryReader.open(directory))

  def search(query: Query): List[MODEL] =
    indexSearcher.search(query, 20).scoreDocs
      .map(sc => indexSearcher.doc(sc.doc))
      .map(doc => convertDocument(doc))
      .toList

  def search(query: String, defaultFields: Array[String]): List[MODEL] =
    this.search(createMultiFieldQuery(defaultFields, query))


  def search(query: String, defaultField: String): List[MODEL] =
    this.search(query, Array(defaultField))

  def getById(id: String): Option[MODEL] =
    this.search(new TermQuery(new Term("id", id)))
    match {
      case head :: tail => Some(head)
      case _ => None
    }

  def createRangeQuery(fieldName: String, begin: DateTime, end: DateTime) : Query =
    createRangeQuery(fieldName, begin.toString, end.toString)

  def createRangeQuery(fieldName: String, begin: String, end: String) : Query =
    new TermRangeQuery(fieldName, new BytesRef(end), new BytesRef(end), true, true)

  def createNumericRangeQuery(fieldName: String, begin: Int, end: Int = Int.MaxValue) : Query =
    NumericRangeQuery.newIntRange(fieldName, begin, end, true, true)

//  def createPhraseQuery(fieldName: String, values: Array[String]) : Query = {
//    val builder = new PhraseQuery.Builder()
//    values.map{ new Term(fieldName,_) }
//      .foreach { builder.add(_) }
//    builder.build()
//  }

//  def createTermQuery(fieldName: String, value: String) : Query =
//    new TermQuery(new Term(fieldName, value))

  def createMultiFieldQuery(fields: Array[String], value: String) : Query = {
    val parser = new MultiFieldQueryParser(fields, analyzer)
    parser.setDefaultOperator(QueryParser.Operator.OR)
    parser.setAllowLeadingWildcard(true);
    parser.setEnablePositionIncrements(true);
    parser.setLowercaseExpandedTerms(true);
    parser.setPhraseSlop(5);

    parser.parse(value)
  }

  def createFieldQuery(field: String, value: String) : Query = {
    val parser = new QueryParser(field, analyzer)
    parser.setDefaultOperator(QueryParser.Operator.OR)
    parser.setAllowLeadingWildcard(true);
    parser.setEnablePositionIncrements(true);
    parser.setLowercaseExpandedTerms(true);
    parser.setPhraseSlop(5);

    parser.parse(value)
  }
}
