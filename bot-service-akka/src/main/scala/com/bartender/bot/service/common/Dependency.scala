package com.bartender.bot.service.common

import com.bartender.bot.service.api.ai.{ApiAiClientHttp, ApiAiResponseGenerator}
import com.bartender.bot.service.fb.{FbMessageSender, FbMessengerSendApiClientHttp, FbMessengerService}
import com.bartender.bot.service.google.{GoogleBarResearcher, GooglePlacesClientHttp}
import com.bartender.bot.service.services.{CocktailDaoMemory, MemoryBarDao, MemoryDao, MessageReceiverImpl}
import com.bartender.bot.service.thecocktaildb.{ThecocktaildbClientHttp, ThecocktaildbCocktailResearcher}

trait Dependency {
  val senderFbClient = new FbMessengerSendApiClientHttp()
  val sender = new FbMessageSender(senderFbClient)
  val dao = new MemoryDao()

  val apiAiClient = new ApiAiClientHttp()
  val responseGenerator = new ApiAiResponseGenerator(apiAiClient)
  val thecocktaildbClient = new ThecocktaildbClientHttp()
  val googlePlacesApiClient = new GooglePlacesClientHttp()
  val barDao = new MemoryBarDao()
  val barResearcher = new GoogleBarResearcher(googlePlacesApiClient, barDao)
  val cocktailDao = new CocktailDaoMemory()
  val cocktailResearcher = new ThecocktaildbCocktailResearcher(thecocktaildbClient, cocktailDao)
  val receiver = new MessageReceiverImpl(sender, responseGenerator, barResearcher, cocktailResearcher)
  val fbMessengerService = new FbMessengerService(receiver)
}
