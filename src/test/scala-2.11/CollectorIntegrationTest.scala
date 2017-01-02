import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.inject.IntegrationTest
import com.twitter.inject.app.TestInjector
import melnyk.co.model.{DataRow, PebbleInput}
import melnyk.co.modules.ActorSystemModule

import scala.concurrent.Await
import scala.concurrent.duration._

class CollectorIntegrationTest extends IntegrationTest {
  override val injector = TestInjector(
    modules = Seq(ActorSystemModule)
  )
  implicit val timeout = Timeout(10.seconds)

  "ActorModule" should {
    "be instant with TestInjector" in {  // test health check
      val actor = injector.instance[ActorRef]("middleware")

      actor should be(an[ActorRef])
    }

    "send back parsed entity" in {
      val actor = injector.instance[ActorRef]("middleware")

      val result = Await.result(actor ? PebbleInput("2016-05-25T21:22:00Z,120,0,4,9915,1,0"), 5.seconds)
      result should be(an[Option[DataRow]])
      result should not be eq(None)
      result.asInstanceOf[Option[DataRow]].get.steps shouldBe 120
    }
  }

}
