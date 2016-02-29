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

  def search(query: Query, size: Int): List[MODEL] =
    indexSearcher.search(query, size).scoreDocs
      .map(sc => indexSearcher.doc(sc.doc))
      .map(doc => convertDocument(doc))
      .toList

  def search(fields: Array[String], query: String, size: Int): List[MODEL] =
    this.search(createMultiFieldQuery(fields, query), size)


  def search(field: String, query: String, size: Int): List[MODEL] =
    this.search(createFieldQuery(field, query), size)

  def getById(id: String): Option[MODEL] =
    this.search(new TermQuery(new Term("id", id)), 1)
    match {
      case List(head: MODEL) => Some(head)
      case _ => None
    }

  def createRangeQuery(field: String, begin: DateTime, end: DateTime) : Query =
    createRangeQuery(field, begin.toString, end.toString)

  def createRangeQuery(field: String, begin: String, end: String) : Query =
    new TermRangeQuery(field, new BytesRef(begin), new BytesRef(end), true, true)

  def createNumericRangeQuery(field: String, begin: Int, end: Int = Int.MaxValue) : Query =
    NumericRangeQuery.newIntRange(field, begin, end, true, true)

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
    parser.setDefaultOperator(QueryParser.Operator.AND)
    parser.setAllowLeadingWildcard(true);
    parser.setEnablePositionIncrements(true);
    parser.setLowercaseExpandedTerms(true);

    parser.parse(value)
  }
}
