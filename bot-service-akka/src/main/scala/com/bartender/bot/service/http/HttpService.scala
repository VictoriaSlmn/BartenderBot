package com.bartender.bot.service.http

import akka.http.scaladsl.server.Directives._
import com.bartender.bot.service.common.{Config, Dependency}


object HttpService extends Config with Dependency {
  val route = pathPrefix(apiVersion) {
    path(infoRoute) {
      complete(s"Hello to bartender bot api($apiVersion) :)")
    } ~
      fbMessengerService.route
  }
}
