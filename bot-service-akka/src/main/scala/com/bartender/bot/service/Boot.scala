package com.bartender.bot.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.bartender.bot.service.common.{Config, Logging}
import com.bartender.bot.service.http.HttpService
import com.bartender.bot.service.instagram.InstagramService

object Boot extends App with Config with Logging {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(HttpService.route, httpHost, httpPort)
  rootLogger.info(s"Server online at $httpHost")

  rootLogger.info("activating to send random cocktail every day..")
  InstagramService.sendRandomCocktailEveryDay()
}
