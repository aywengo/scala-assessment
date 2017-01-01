package melnyk.co.middleware

import akka.actor.Actor
import melnyk.co.model.{DataRow, PebbleInput}

class PebbleActor extends Actor {
  override def receive: Receive = {
    case PebbleInput(data) => sender() ! DataRow.fromInput(data)
  }
}
