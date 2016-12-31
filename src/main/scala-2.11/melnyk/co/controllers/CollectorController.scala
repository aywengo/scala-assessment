package melnyk.co.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class CollectorController extends Controller{
  post("/import") { req: Request =>
    info(s"Got content from ${req.remoteAddress}: ${req.encodeString}")
    info(s"Got params from ${req.remoteAddress}: ${req.getParams()}")
    s"{ data: ${req.contentString} }"
  }
}
