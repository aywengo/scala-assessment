import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest
import melnyk.co.Server
import melnyk.co.model.DataRow
import melnyk.co.services.DalService


class CollectorFeatureTest extends FeatureTest with Mockito {

  override val server = new EmbeddedHttpServer(new Server)

  @Bind val dalService: DalService = smartMock[DalService]

  "Server" should {
    "Accept import Post" in {
      server.httpFormPost(
        path = "/import",
        params = Map("data" -> "2016-05-25T21:22:00Z,120,0,4,9915,1,0"),
        andExpect = Ok,
        withJsonBody = """{"time":"2016-05-25T21:22:00.000Z","steps":120,"yaw":0,"pitch":4,"vmc":9915,"light":1,"activity":0}"""
      )
    }

    "Accept import Post with Actor per Request approach" in {
      server.httpFormPost(
        path = "/v2/import",
        params = Map("data" -> "2016-05-25T21:22:00Z,120,0,4,9915,1,0"),
        andExpect = Ok,
        withJsonBody = """{"time":"2016-05-25T21:22:00.000Z","steps":120,"yaw":0,"pitch":4,"vmc":9915,"light":1,"activity":0}"""
      )
    }

    "Accept trace Put" in {
      server.httpPut(
        path = "/trace",
        putBody =
          """
            |{
            |   "log" : "Log entry"
            |}
            | """.stripMargin,
        andExpect = Ok
      )
    }

    "Accept trace Put with Actor per Request approach" in {
      server.httpPut(
        path = "/v2/trace",
        putBody =
          """
            |{
            |   "log" : "Log entry"
            |}
            | """.stripMargin,
        andExpect = Ok
      )
    }

    "Returns collection" in {
      dalService.fetchData returns List(DataRow.fromInput("2016-05-25T21:22:00Z,120,0,4,9915,1,0"))

      server.httpGetJson[List[DataRow]](
        path = "/data",
        andExpect = Ok,
        withJsonBody = """[{"activity":0,"light":1,"pitch":4,"steps":120,"time":"2016-05-25T21:22:00.000Z","vmc":9915,"yaw":0}]"""
      )
    }
  }
}