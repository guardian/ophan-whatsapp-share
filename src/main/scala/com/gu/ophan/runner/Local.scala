package com.gu.ophan.runner

import com.gu.ophan._
import org.joda.time.{DateTime, DateTimeZone}

object Local extends App {

  val today = DateTime.now.minusHours(2).withZone(DateTimeZone.forID("Europe/London"))
  
  val path = today.toString("yyyy-MM-dd-HH").toLowerCase

  println(path)

  val html = ReportGenerator.generateReportForDate(today)

  import java.io._
  val pw = new PrintWriter(new File(s"$path.json"))
  pw.write(html)
  pw.close

} 