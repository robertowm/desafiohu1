package com.hotelurbano.desafiohu1

import com.twitter.finagle.http.Status.Ok
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class AppFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new AppServer)

  "Server" should {
    "get widget" in {
      server.httpGet(
        path = "/widget",
        andExpect = Ok
      )
    }

    "autocomplete 'penedo'" in {
      server.httpGet(
        path = "/widget/autocomplete?query=penedo",
        andExpect = Ok,
        withBody = "[\"Penedo\",\"Capitao Nareia Pousada - Penedo\",\"Hotel Bellevue - Penedo\",\"City Seasons Al Ain - Penedo\",\"Marquesado De Almansa - Penedo\",\"Apartamentos Aguazul - Penedo\"]"
      )
    }

    "autocomplete 'hotel'" in {
      server.httpGet(
        path = "/widget/autocomplete?query=hotel",
        andExpect = Ok,
        withBody = "[\"Kananxue Hotel - Resende\",\"Hotel Bellevue - Penedo\",\"155 Hotel - Itatiaia\",\"Hotel Banyan - Itatiaia\",\"Hotel Elisabethpark - Itatiaia\"]"
      )
    }

    "autocomplete 'abobrinha' and find nothing" in {
      server.httpGet(
        path = "/widget/autocomplete?query=abobrinha",
        andExpect = Ok,
        withBody = "[]"
      )
    }

    "search for 'Penedo' and respond an html" in {
      server.httpPost(
        path = "/search",
        postBody = "responseType=view&where=Penedo&begin=06%2F05%2F2015&end=08%2F05%2F2015",
        contentType = "application/x-www-form-urlencoded",
        andExpect = Ok
      )
    }

    "search for 'Penedo' and respond a json" in {
      server.httpPost(
        path = "/search",
        postBody = "where=Penedo&begin=06%2F05%2F2015&end=08%2F05%2F2015",
        contentType = "application/x-www-form-urlencoded",
        andExpect = Ok,
        withBody = "[{\"hotel_id\":\"418\",\"name\":\"Grand Kurdoglu Hotel\",\"city\":\"Penedo\",\"begin\":\"2015-05-06T03:00:00.000Z\",\"end\":\"2015-05-08T03:00:00.000Z\",\"available_rooms\":1},{\"hotel_id\":\"10\",\"name\":\"Capitao Nareia Pousada\",\"city\":\"Penedo\",\"begin\":\"2015-05-06T03:00:00.000Z\",\"end\":\"2015-05-08T03:00:00.000Z\",\"available_rooms\":1},{\"hotel_id\":\"348\",\"name\":\"Apartamentos Aguazul\",\"city\":\"Penedo\",\"begin\":\"2015-05-06T03:00:00.000Z\",\"end\":\"2015-05-08T03:00:00.000Z\",\"available_rooms\":1},{\"hotel_id\":\"25\",\"name\":\"City Seasons Al Ain\",\"city\":\"Penedo\",\"begin\":\"2015-05-06T03:00:00.000Z\",\"end\":\"2015-05-08T03:00:00.000Z\",\"available_rooms\":1},{\"hotel_id\":\"464\",\"name\":\"Maritim Hotel Club Alantur\",\"city\":\"Penedo\",\"begin\":\"2015-05-06T03:00:00.000Z\",\"end\":\"2015-05-08T03:00:00.000Z\",\"available_rooms\":1}]"
      )
    }

    "search for 'Mercatto Casa Hotel, Araruama' and respond an html" in {
      server.httpPost(
        path = "/search",
        postBody = "responseType=view&where=Mercatto+Casa+Hotel+-+Araruama&begin=06%2F05%2F2015&end=08%2F05%2F2015",
        contentType = "application/x-www-form-urlencoded",
        andExpect = Ok
      )
    }

    "search for 'Mercatto Casa Hotel, Araruama' and respond a json" in {
      server.httpPost(
        path = "/search",
        postBody = "where=Mercatto+Casa+Hotel+-+Araruama&begin=06%2F05%2F2015&end=08%2F05%2F2015",
        contentType = "application/x-www-form-urlencoded",
        andExpect = Ok,
        withBody = "[{\"hotel_id\":\"1\",\"name\":\"Mercatto Casa Hotel\",\"city\":\"Araruama\",\"begin\":\"2015-05-06T03:00:00.000Z\",\"end\":\"2015-05-08T03:00:00.000Z\",\"available_rooms\":1}]"
      )
    }
  }
}
