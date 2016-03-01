package com.hotelurbano.desafiohu1.model

import org.joda.time.DateTime

case class AvailablePackage (hotelId: String, name: String, city: String,
                        begin: DateTime, end: DateTime, availableRooms: Int)

