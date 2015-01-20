package com.github.playforscala.barcodes

//import play.api.mvc.{ Action, Controller }
import akka.actor.{ ActorRef }
import scala.concurrent.Future
import akka.util.Timeout
import scala.util.Try
import scala.concurrent.duration._
import akka.pattern.ask
import play.api.libs.concurrent.Execution.Implicits._

object Barcodes {

  var barcodeCache: ActorRef = _
  
  val MimeType = "image/png"
  val imageResolution = 144
  
  def renderImage(ean: Long): Future[Try[Array[Byte]]] = {
    implicit val timeout = Timeout(20.seconds)
    
    barcodeCache ? RenderImage(ean) map {
      case RenderResult(result) => result
    }
  }
  
//  def barcode(ean: Long) = Action{
//    
//    import java.lang.IllegalArgumentException
//    
//    val MimeType = "image/png"
//    Try (ean13BarCode(ean, MimeType)) match {
//      case Success(imageData) => Ok(imageData).as(MimeType)
//      case Failure(e) => BadRequest("couldn't generate barcode: " + e.getMessage())
//    }
//  }
  
}