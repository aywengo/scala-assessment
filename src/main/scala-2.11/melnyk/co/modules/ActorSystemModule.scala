package melnyk.co.modules

import akka.actor.{ActorRef, ActorSystem, Props}
import com.twitter.inject.TwitterModule
import melnyk.co.middleware.PebbleActor

object ActorSystemModule extends TwitterModule{

  override def configure(): Unit = {
    super.configure()

    val system = ActorSystem("PebbleCollectorAS")
    val middleWare: ActorRef = system.actorOf(Props[PebbleActor])
    bind[ActorRef].annotatedWithName("middleware").toInstance(middleWare)
  }
}
