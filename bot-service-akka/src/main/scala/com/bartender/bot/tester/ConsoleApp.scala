package com.bartender.bot.tester

import com.bartender.bot.service.api.ai.{ApiAiClientHttp, ApiAiResponseGenerator}
import com.bartender.bot.service.domain.{Bar, Location, Message, Recipient}
import com.bartender.bot.service.google.{GoogleBarResearcher, GooglePlacesClientHttp}
import com.bartender.bot.service.instagram.InstagramClient
import com.bartender.bot.service.instagram.InstagramService._
import com.bartender.bot.service.services._
import com.bartender.bot.service.thecocktaildb.{ThecocktaildbClientHttp, ThecocktaildbCocktailResearcher}
import org.brunocvcunha.instagram4j.Instagram4j

import scala.language.postfixOps

object ConsoleApp {
  val patternLocation = "l(\\d{1,3}\\.\\d+),(\\d{1,3}\\.\\d+)"
  private lazy val instagramClient = {
    val instagram4j = Instagram4j.builder()
      .username(instagramConfig.getString("username"))
      .password(instagramConfig.getString("password"))
      .build()
    instagram4j.setup()
    instagram4j.login()
    new InstagramClient(instagram4j, cocktailResearcher)
  }

  def main(args: Array[String]): Unit = {
    val sender = new ConsoleSender()
    val dao = new MemoryDao()

    val googlePlacesApiClient = new GooglePlacesClientHttp()
    val barDao = new MemoryBarDao()
    val barResearcher = new GoogleBarResearcher(googlePlacesApiClient, barDao)
    val apiAiClient = new ApiAiClientHttp()
    val responseGenerator = new ApiAiResponseGenerator(apiAiClient)
    val thecocktaildbClient = new ThecocktaildbClientHttp()
    val cocktailDao = new CocktailDaoMemory()
    val cocktailResearcher = new ThecocktaildbCocktailResearcher(thecocktaildbClient, cocktailDao)
    val receiver = new MessageReceiverImpl(sender, responseGenerator, barResearcher, cocktailResearcher)
    val recipient = Recipient("test_recipient")

    println("Welcome!")
    println("For searching nearest bars input coordinate as \"l{latitude},{longitude}\"")

    var coordinates: Option[Location] = None
    var lastBar: Option[Bar] = None
    var offset = 0
    while (true) {
      val input = scala.io.StdIn.readLine()
      if (input matches patternLocation) {
        val args = input.substring(1).split(",")
        coordinates = Some(Location(lat = args(0).toDouble, lng = args(1).toDouble))
        lastBar = receiver.receiveNearestBar(coordinates.get, recipient, offset)
      } else if (coordinates.isDefined && input.equals("next")) {
        offset += 1
        lastBar = receiver.receiveNearestBar(coordinates.get, recipient, offset)
      } else if (lastBar.isDefined && input.equals("details")) {
        receiver.receiveBarDetails(lastBar.get.id, recipient)
        lastBar = None
      } else if (input.equals("post")) {
        instagramClient.publishRandomCocktail()
      } else {
        coordinates = None
        lastBar = None
        offset = 0
        val message = Message(input)
        receiver.receive(message, recipient)
      }
    }
  }
}
