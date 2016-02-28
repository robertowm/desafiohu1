package com.hotelurbano.desafiohu1

import com.twitter.finagle.http.Status.Ok
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class AppFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new AppServer)

  "Server" should {
    "ping" in {
      server.httpGet(
        path = "/ping",
        andExpect = Ok,
        withBody = "pong")
    }

    "get widget" in {
      server.httpGet(
        path = "/widget",
        andExpect = Ok
      )
    }

    "autocomplete" in {
      server.httpGet(
        path = "/widget/autocomplete?query=penedo",
        andExpect = Ok,
        withBody = "[\"Penedo\",\"Capitao Nareia Pousada - Penedo\",\"Hotel Bellevue - Penedo\",\"City Seasons Al Ain - Penedo\",\"Marquesado De Almansa - Penedo\",\"Apartamentos Aguazul - Penedo\",\"Akdas Kusadasi - Penedo\",\"Grand Kurdoglu Hotel - Penedo\",\"Maritim Hotel Club Alantur - Penedo\",\"Titan Select Hotel - Penedo\"]"
      )

      server.httpGet(
        path = "/widget/autocomplete?query=abobrinha",
        andExpect = Ok,
        withBody = "[]"
      )
    }
  }
}
