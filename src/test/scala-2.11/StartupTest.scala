
import com.google.inject.Stage
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import melnyk.co.Server

class StartupTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(
    twitterServer = new Server,
    stage = Stage.PRODUCTION,
    verbose = false)

  "Server" should {
    "startup" in {
      server.assertHealthy()
    }
  }
}
