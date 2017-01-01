package melnyk.co.controllers

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.finatra.http.Controller

import scala.concurrent.duration._
import melnyk.co.middleware.PebbleActor
import melnyk.co.model.{DataRow, PebbleInput}
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.util.{Future => TwitterFuture}
import scala.concurrent.{Future => ScalaFuture}

class CollectorController extends Controller{
  val system = ActorSystem("PebbleCollectorAS")
  val middleWare = system.actorOf(Props[PebbleActor])

  implicit val timeout = Timeout(30.seconds)
  def toTwitterFuture(in: ScalaFuture[Any]): TwitterFuture[Any] = in.as[TwitterFuture[Any]]

  post("/import") { req: PebbleInput =>
    toTwitterFuture(middleWare ? req)
  }
}
