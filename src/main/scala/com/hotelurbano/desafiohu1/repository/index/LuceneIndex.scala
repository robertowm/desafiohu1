package com.hotelurbano.desafiohu1.repository.index

import com.hotelurbano.desafiohu1.model.Searchable
import com.ibm.icu.text.Collator
import com.ibm.icu.util.ULocale
import org.apache.lucene.analysis.br.BrazilianAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.util.CharArraySet
import org.apache.lucene.collation.ICUCollationKeyAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.{Term, DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.{MultiFieldQueryParser, QueryParser}
import org.apache.lucene.search._
import org.apache.lucene.store.RAMDirectory

abstract class LuceneIndex[MODEL <: Searchable] {

  protected def loadData(writer: IndexWriter)
  protected def convertDocument(document: Document): MODEL

//  private val analyzer: ICUCollationKeyAnalyzer =
//    new ICUCollationKeyAnalyzer(Collator.getInstance(new ULocale("pt_BR")))
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

  def search(query: String, defaultFields: Array[String]): List[MODEL] = {
    val parser = new MultiFieldQueryParser(defaultFields, analyzer)
    parser.setDefaultOperator(QueryParser.Operator.OR)
    parser.setAllowLeadingWildcard(true);
    parser.setEnablePositionIncrements(true);
    parser.setLowercaseExpandedTerms(true);
    parser.setPhraseSlop(5);

    this.search(parser.parse(query))
  }

  def search(query: String, defaultField: String): List[MODEL] =
    this.search(query, Array(defaultField))

  def getById(id: String): Option[MODEL] =
    this.search(new TermQuery(new Term("id", id)))
    match {
      case head :: tail => Some(head)
      case _ => None
    }
}
