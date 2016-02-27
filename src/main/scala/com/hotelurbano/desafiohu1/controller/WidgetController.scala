package com.hotelurbano.desafiohu1.controller

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class WidgetController extends Controller {

  get("/widget") { request: Request =>
    response.ok.view("search.mustache", null)
  }

  post("/search") { request: Request =>
    response.ok("<h1>Ok!</h1>")
  }

}
