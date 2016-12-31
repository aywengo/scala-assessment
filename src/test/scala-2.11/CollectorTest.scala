import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import melnyk.co.Server

class CollectorTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)

  "Server" should {
    "Accept import Post" in {
      server.httpPost(
        path = "/import",
        postBody = "2016-05-25T21:22:00Z,120,0,4,9915,1,0",
        andExpect = Ok,
        withBody = "{ data: 2016-05-25T21:22:00Z,120,0,4,9915,1,0 }"
      )
    }
  }
}