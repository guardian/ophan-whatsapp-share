package com.gu.ophan

import org.joda.time.DateTime
import java.io.InputStreamReader

object ReportGenerator {

  def generateReportForDate(date: DateTime): String = {

    val bucket = "aws-frontend-logs"
    val prefix = """fastly/www.theguardian.com/""" + date.toString("yyyy-MM-dd") + "T" + date.toString("HH")

    val logfiles = S3.list(bucket, prefix)
    val logentries = logfiles.map(logfile => parse(bucket, logfile)).flatten
    val shares: List[Share] = logentries.groupBy(_.path).mapValues(_.length).map{ case (path, sum) => Share(path, sum) }(collection.breakOut).toList.sortBy(_.total)
    generateJsonFromShares(shares)

  }

  def parse(bucket: String, path: String): List[LogEntry] = {
    val is = S3.get(bucket, path).getObjectContent
    val src = scala.io.Source.fromInputStream(is)(io.Codec("iso-8859-1"))
    val logEntries = Parser.parse(src).toList
    src.close
    logEntries
  }

  private def generateJsonFromShares(shares: List[Share]): String = {
    val entries: String = shares.map(share => s""" { "path": "${share.path}", "total": ${share.total} } """).mkString(",")

    s"""
      |{
      | "Shares" : [${entries}]
      | }
    """.stripMargin
  }

}


