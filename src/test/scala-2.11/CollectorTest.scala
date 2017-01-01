import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import melnyk.co.Server

class CollectorTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)

  "Server" should {
    "Accept import Post" in {
      server.httpFormPost(
        path = "/import",
        params = Map("data" -> "2016-05-25T21:22:00Z,120,0,4,9915,1,0"),
        //postBody = "2016-05-25T21:22:00Z,120,0,4,9915,1,0",
        andExpect = Ok,
        withBody = """{"time":"2016-05-25T21:22:00.000Z","steps":120,"yaw":0,"pitch":4,"vmc":9915,"light":1,"activity":0}"""
      )
    }
  }
}