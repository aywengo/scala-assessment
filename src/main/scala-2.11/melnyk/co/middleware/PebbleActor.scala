package melnyk.co.middleware

import akka.actor.Actor
import com.twitter.inject.Logging
import melnyk.co.model.{DataRow, PebbleInput, Trace}

import scala.language.implicitConversions

class PebbleActor extends Actor with Logging {
  override def receive: Receive = {
    case PebbleInput(data) => sender() ! DataRow.fromInput(data)
    case Trace(in) => info(s"Got request to trace: $in"); sender() ! in
    case _ => error("Unpredicted handler in PebbleActor")
  }
}
