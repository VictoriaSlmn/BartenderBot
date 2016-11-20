package com.bartender.bot.service.fb

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, StandardRoute, ValidationRejection}
import com.bartender.bot.service.common.{Config, Logging}
import com.bartender.bot.service.domain.{Message, Recipient}
import com.bartender.bot.service.services.MessageReceiver

class FbMessengerService(receiver: MessageReceiver) extends Directives with JsonSupport with Config with Logging {
  val route = path(fbMessengerWebhook) {
    get {
      parameters("hub.mode", "hub.verify_token" ?, "hub.challenge" ?) {
        validateWebhook
      }
    } ~
      post {
        /*  entity(as[FbMessengerResponse]) { response =>
            val entry = response.entry.last
            rootLogger.info(s"last entry: $entry")
            val messaging = entry.messaging.last
            rootLogger.info(s"last messaging: $messaging")
            val userMessage = messaging.message
            val result: String = userMessage.text match {
              case Some(text) => text
              case None =>
                val attachment = userMessage.attachments.get.last
                attachment.`type` match {
                  case AttachmentType.location => attachment.payload.coordinates.last.toString
                  case _ => attachment.payload.url.get
                }
            }
            rootLogger.info(s"user message: $userMessage")
            FbMessengerSendApiClient.sendTextMessage(Recipient(messaging.sender.id), result)
            complete(result)
          } */
        /*
                extractRequestEntity { response =>
                  rootLogger.info(s"$response")
                  complete("OK")
                }
        */

        entity(as[FbMessengerHookBody]) { hookBody =>
          rootLogger.info(s"$hookBody")
          val userMessage = hookBody.entry.last.messaging.last.message

          if (userMessage != None) {
            val messageTextOption = userMessage.get.text
            if (messageTextOption != None) {
              val message = new Message(messageTextOption.get)
              val recipient = new Recipient(hookBody.entry.last.messaging.last.sender.id)

              receiver.Receive(message, recipient)
            }
          }

          complete("Ok")
        }

      }
  }

  private def validateWebhook: (String, Option[String], Option[String]) => StandardRoute = {
    (mode, token, challenge) =>
      if (mode == "subscribe" && token.getOrElse("").equals(fbMessengerVerifyToken)) {
        rootLogger.info("Validating webhook")
        complete(challenge)
      } else {
        rootLogger.error("Failed validation. Make sure the validation tokens match.")
        reject(ValidationRejection("Verify token not correct!"))
      }
  }
}
