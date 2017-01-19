package melnyk.co.controllers

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorRef, ActorSystem, Props}
import com.google.inject.name.Named
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.utils.FuturePools
import melnyk.co.actors.PebbleActor
import melnyk.co.model._
import melnyk.co.services.DalService
import melnyk.co.utils.AkkaExtensions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

@Singleton
class CollectorController @Inject() (@Named("middleware") actor: ActorRef, @Named("ActorSystem") actorSys: ActorSystem, dal: DalService)
  extends Controller with AkkaExtensions {

  private val futurePool = FuturePools.unboundedPool("CallbackConverter")

  post("/import") { req: PebbleInput =>
    -?>(actor,req)
  }

  put("/trace") { req: Trace =>
    -!>(actor,req)
  }


  implicit val actorSystem: ActorSystem = actorSys
  post("/v2/import") { req: PebbleInput =>
    --?>(Props[PebbleActor],req)
  }

  put("/v2/trace") {req: Trace =>
    --!> (Props[PebbleActor], req)
  }

  get("/data"){ _: Request =>
    futurePool {
      dal.fetchData()
    }
  }
}
