package com.gu.ophan.runner

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.gu.ophan.{S3, ReportGenerator}
import org.joda.time.{DateTime, DateTimeZone}

class Lambda {

  def generateReport(event: SNSEvent, context: Context): Unit = {

    val logger = context.getLogger
    val target = "ophan-whatsapp-share"

    logger.log(s"Starting report at ${DateTime.now}")

    val today = DateTime.now.minusHours(2).withZone(DateTimeZone.forID("Europe/London"))
    val path = today.toString("yyyy-MM-dd-HH").toLowerCase


    val json = ReportGenerator.generateReportForDate(today)
    S3.put(target, s"${path}.json", json)

    logger.log(s"Ending report at ${DateTime.now}")
    
  }

}