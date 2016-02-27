package com.hotelurbano.desafiohu1

import com.google.inject.Stage
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.Test

class AppStartupTest extends Test {

  val server = new EmbeddedHttpServer(
    stage = Stage.PRODUCTION,
    twitterServer = new AppServer)

  "server" in {
    server.assertHealthy()
  }
}
