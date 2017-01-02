package melnyk.co.controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorRef
import com.google.inject.name.Named
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.utils.FuturePools
import melnyk.co.model._
import melnyk.co.services.DalService
import melnyk.co.utils.AkkaExtensions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

@Singleton
class CollectorController @Inject() (@Named("middleware") actor: ActorRef,
                                     dal: DalService)
  extends Controller with AkkaExtensions{

  private val futurePool = FuturePools.unboundedPool("CallbackConverter")

  post("/import") { req: PebbleInput =>
    -?>(actor,req)
  }

  patch("/trace") { req: PebbleInput =>
    -!>(actor,req.data)
  }

  get("/data"){ _: Request =>
    futurePool {
      dal.fetchData()
    }
  }
}
