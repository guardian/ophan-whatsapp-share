package com.gu.ophan


object Parser {

  def parse(src: scala.io.Source): Iterator[LogEntry] = src.getLines.map(LogEntry.fromLine(_)).filter(log => log.status != 404 && log.userAgent.startsWith("""WhatsApp/""")) 

}

case class LogEntry(path: String, status: Int, userAgent: String)

object LogEntry {
  def fromLine(line: String): LogEntry = {
    val columns = line.split('\t')
    val path = columns(5).split(' ')(0)
    val status = columns(6).toInt
    val userAgent = columns(9) 
    LogEntry(path, status, userAgent)
  }
}