import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import melnyk.co.middleware.PebbleActor
import melnyk.co.model.{DataRow, PebbleInput}
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class PebbleActorTest extends TestKit(ActorSystem("PebbleActorTest")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val timeout = Timeout(30.seconds)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val actor = system.actorOf(Props[PebbleActor])

  "PebbleActor" must {

    "send back parsed entity" in {
      actor ! PebbleInput("2016-05-25T21:22:00Z,120,0,4,9915,1,0")
      expectMsg(Some(DataRow(new DateTime(2016, 5,25, 21, 22),120,0,4,9915,1,0)))
    }

  }
}
