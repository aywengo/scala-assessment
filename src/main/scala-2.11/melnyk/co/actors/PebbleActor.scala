package melnyk.co.actors

import akka.actor.Actor
import com.twitter.inject.Logging
import melnyk.co.model.{DataRow, PebbleInput, Trace}

import scala.language.implicitConversions

class PebbleActor extends Actor with Logging {
  override def receive: Receive = {
    case PebbleInput(data) => debug(s"Got request to parse PebbleInput: $data"); sender() ! DataRow.fromInput(data)
    case Trace(in) => debug(s"Got request to trace: $in"); sender() ! in
    case _ => error("Unpredicted handler in PebbleActor")
  }
}
