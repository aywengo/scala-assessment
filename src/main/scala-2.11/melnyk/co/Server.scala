package melnyk.co

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import melnyk.co.controllers.CollectorController
import melnyk.co.modules.ActorSystemModule

object ServerMain extends Server

class Server extends HttpServer {

  override val modules = Seq(ActorSystemModule)

  override def configureHttp(router: HttpRouter) =
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[CollectorController]
}
