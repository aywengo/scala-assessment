package melnyk.co.controllers

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import melnyk.co.middleware.PebbleActor
import melnyk.co.model._
import melnyk.co.utils.AkkaExtensions
import com.twitter.finatra.utils.FuturePools

import scala.concurrent.duration._
import scala.language.implicitConversions

class CollectorController extends Controller with AkkaExtensions{
  val system = ActorSystem("PebbleCollectorAS")
  val middleWare: ActorRef = system.actorOf(Props[PebbleActor])
  private val futurePool = FuturePools.unboundedPool("CallbackConverter")

  implicit val timeout = Timeout(30.seconds)

  post("/import") { req: PebbleInput =>
    ->(middleWare ? req)
  }

  get("/data"){ _: Request =>
    futurePool {
      List(
        DataRow.fromInput("2016-05-25T21:22:00Z,120,0,4,9915,1,0"),
        DataRow.fromInput("2016-05-25T21:23:00Z,10,0,4,9915,1,0"),
        DataRow.fromInput("2016-05-25T21:24:00Z,20,0,4,9915,1,0"),
        DataRow.fromInput("2016-05-25T21:25:00Z,12,0,4,9915,1,0"),
        DataRow.fromInput("2016-05-25T21:26:00Z,,,1,,1,0")
      )
    }
  }
}
