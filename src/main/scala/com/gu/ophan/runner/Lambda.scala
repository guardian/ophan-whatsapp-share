package com.gu.ophan.runner

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.gu.ophan.{S3, ReportGenerator}
import org.joda.time.{DateTime, DateTimeZone}

class Lambda {

  def generateReport(event: SNSEvent, context: Context): Unit = {

    val logger = context.getLogger
    val target = "ophan-whatsapp-share"


    val days = List.range(1, 7).map(day => DateTime.now.minusDays(day).minusHours(2).withZone(DateTimeZone.forID("Europe/London")))
    for (today <- days) {
        
        val path = today.toString("yyyy-MM-dd-HH").toLowerCase

        logger.log(s"Starting report at ${DateTime.now} for ${path}")
        
        val json = ReportGenerator.generateReportForDate(today)
        S3.put(target, s"${path}.json", json)

        logger.log(s"Ending report at ${DateTime.now} for ${path}")

    }
    
  }

}