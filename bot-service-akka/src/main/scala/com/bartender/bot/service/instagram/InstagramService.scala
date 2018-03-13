package com.bartender.bot.service.instagram

import java.io.File
import java.net.URL

import akka.actor.ActorSystem
import com.bartender.bot.service.common.{Config, Dependency, Logging}
import com.bartender.bot.service.services.CocktailResearcher
import org.apache.commons.io.FileUtils
import org.brunocvcunha.instagram4j.Instagram4j
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class InstagramClient(instagramApi: Instagram4j,
                      cocktailResearcher: CocktailResearcher) extends Logging {

  def publishRandomCocktail(): Boolean = cocktailResearcher.cocktailReceipt(None).exists(cocktail =>
    cocktail._1.imageUrl.exists(url => {
      val destination = new File("/tmp/cocktail_of_day.jpg")
      rootLogger.info(s"Cocktail picture uploded from $url")
      FileUtils.copyURLToFile(new URL(url), destination)
      rootLogger.info(s"Instagram send request")
      try {
        val result = instagramApi.sendRequest(new InstagramUploadPhotoRequest(destination,
          s"""| ${cocktail._1.name}
              | ${
            cocktail._2.ingredients.map(ingredient =>
              s"- ${ingredient.name} ${
                if (ingredient.measure.trim.nonEmpty) {
                  s"(${ingredient.measure})"
                } else {
                  ""
                }
              }").mkString("Ingredients: ", "; ", ".")
          }
              | ${cocktail._2.instruction.map(_.toString).getOrElse("")}
              | #${cocktail._1.name.replaceAll(" ", "")} #cocktail #coctailofday #cocktails #tony🍹
          """.stripMargin))
        rootLogger.info(s"uploading result: ${result.toString}")
      } catch {
        case e: Exception => rootLogger.error(e.getMessage)
      }
      return true
    }))
}

object InstagramService extends Config with Dependency with Logging {
  private lazy val instagramClient = {
    val instagram4j = Instagram4j.builder()
      .username(instagramConfig.getString("username"))
      .password(instagramConfig.getString("password"))
      .build()
    rootLogger.info("Instagram setup")
    instagram4j.setup()
    rootLogger.info("Instagram login")
    try {
      val result = instagram4j.login()
      rootLogger.info(s"Instagram login result: ${result.toString}")
    }
    catch {
      case e: Exception => rootLogger.error(e.getMessage)
    }
    new InstagramClient(instagram4j, cocktailResearcher)
  }

  def sendRandomCocktailEveryDay(): Unit = {
    val system = ActorSystem("MySystem")
    system.scheduler.schedule(0 day, 1 day) {
      rootLogger.info("Publish random cocktail..")
      val result = instagramClient.publishRandomCocktail()
      if (result) rootLogger.info("Publish random cocktail was successful")
      else rootLogger.info("Instagram post was not created")
    }
  }
}

