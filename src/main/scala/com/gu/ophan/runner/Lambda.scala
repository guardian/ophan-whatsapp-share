package com.gu.ophan.runner

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.gu.ophan.{S3, ReportGenerator}
import org.joda.time.{DateTime, DateTimeZone}

class Lambda {

  def generateReport(event: SNSEvent, context: Context): Unit = {

    val logger = context.getLogger
    val target = "ophan-whatsapp-share"

    /*logger.log(s"Starting report at ${DateTime.now}")
    logger.log(s"event: ${event}")
    logger.log(s"records: ${event.getRecords()}")
    logger.log(s"first record: ${event.getRecords().get(0)}")
    logger.log(s"message: ${event.getRecords().get(0).getSNS().getMessage}")*/



    val today = DateTime.now.minusHours(2).withZone(DateTimeZone.forID("Europe/London"))
    val path = today.toString("yyyy-MM-dd-HH").toLowerCase


    val json = ReportGenerator.generateReportForDate(today)
    S3.put(target, s"${path}.json", json)


    logger.log(s"Ending report at ${DateTime.now}")

    val month = System.getenv("MONTH")
    val day = System.getenv("DAY")

    if (month != null && day != null) {
      val hours = List.range(0, 23)
      for (h <- hours) {
        val tz = DateTimeZone.forID("Europe/London")
        val d =  new DateTime(2020, month.toInt, day.toInt, h, 0, tz)
        logger.log(s"Generate report for ${today.toString("yyyy-MM-dd-HH")}")
        gen(target, d)
        logger.log(s"Ending report at ${DateTime.now}")
      }
    }
  }

  def gen(target: String, today: DateTime) {
    val path = today.toString("yyyy-MM-dd-HH").toLowerCase
    val json = ReportGenerator.generateReportForDate(today)
    S3.put(target, s"${path}.json", json)
  }

}