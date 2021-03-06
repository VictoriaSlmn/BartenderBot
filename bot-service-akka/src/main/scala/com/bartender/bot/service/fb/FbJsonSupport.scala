package com.bartender.bot.service.fb

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

trait FbJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object AttachmentTypeFormat extends RootJsonFormat[FbAttachmentType.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbAttachmentType.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbAttachmentType.Value = json match {
      case JsString(str) => FbAttachmentType.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit object TemplateTypeFormat extends RootJsonFormat[FbTemplateType.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbTemplateType.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbTemplateType.Value = json match {
      case JsString(str) => FbTemplateType.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit object FbTemplateButtonsTypeFormat extends RootJsonFormat[FbTemplateButtonsType.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbTemplateButtonsType.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbTemplateButtonsType.Value = json match {
      case JsString(str) => FbTemplateButtonsType.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit object FbTopElementListTemplateTypeFormat extends RootJsonFormat[FbTopElementListTemplateType.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbTopElementListTemplateType.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbTopElementListTemplateType.Value = json match {
      case JsString(str) => FbTopElementListTemplateType.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit val coordinates = jsonFormat2(FbCoordinates)
  implicit val fbTemplateButtonsFormat = jsonFormat4(FbTemplateButton)
  implicit val fbTemplateElementDefaultActionFormat = jsonFormat2(FbTemplateElementDefaultAction)
  implicit val fbTemplateElementsFormat = jsonFormat5(FbTemplateElement)
  implicit val payloadFormat = jsonFormat8(FbPayload)
  implicit val attachmentFormat = jsonFormat2(FbAttachment)
  implicit val quickReplyResponseFormat = jsonFormat1(FbQuickReplyResponse)
  implicit val messageFormat = jsonFormat6(FbMessage)
  implicit val serviceMessageFormat = jsonFormat3(FbServiceMessage)
  implicit val fbButtonPostbackFormat = jsonFormat1(FbButtonPostback)
  implicit val recipientFormat = jsonFormat1(FbRecipient)
  implicit val senderFormat = jsonFormat1(FbSender)
  implicit val messagingFormat = jsonFormat7(FbMessaging)
  implicit val entryFormat = jsonFormat3(FbEntry)
  implicit val fbMessengerResponseFormat = jsonFormat2(FbMessengerHookBody)


  /**** send api ****/

  implicit object QuickReplyTypeFormat extends RootJsonFormat[FbQuickReplyType.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbQuickReplyType.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbQuickReplyType.Value = json match {
      case JsString(str) => FbQuickReplyType.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit object NotificationTypeFormat extends RootJsonFormat[FbNotificationType.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbNotificationType.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbNotificationType.Value = json match {
      case JsString(str) => FbNotificationType.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit object SenderActionFormat extends RootJsonFormat[FbSenderAction.Value] { //todo: maybe possible create common method for parse enum
  def write(obj: FbSenderAction.Value): JsValue = JsString(obj.toString)

    def read(json: JsValue): FbSenderAction.Value = json match {
      case JsString(str) => FbSenderAction.withName(str)
      case _ => throw DeserializationException("Enum string expected")
    }
  }

  implicit val quickReplyRequestFormat = jsonFormat4(FbQuickReplyRequest)
  implicit val sendMessageFormat = jsonFormat4(FbSendMessage)
  implicit val fbMessengerRequestFormat = jsonFormat4(FbMessengerRequest)

}
