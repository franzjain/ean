package com.github.playforscala.barcodes

import akka.actor.Actor
import scala.concurrent._
import scala.util.Try
import play.api.libs.concurrent.Execution.Implicits._

class BarcodeCache extends Actor {

  val ImageResolution = 144
  
  var imageCache = Map[Long, Future[Array[Byte]]]()
  
  def receive = {
    case RenderImage(ean) =>
      val futureImage = imageCache.get(ean) match {
        case Some(futureImage) => futureImage
        case None => {
          val futureImage = future { ean13BarCode(ean, "image/png") }
          imageCache += (ean -> futureImage)
          futureImage
        }
      }
      
      val client = sender
      futureImage.onComplete { 
        client ! RenderResult(_)
      }
  }
  
  def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {
    
    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage
    import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
    import org.krysalis.barcode4j.impl.upcean.EAN13Bean
    
    val output: ByteArrayOutputStream = new ByteArrayOutputStream
    val canvas: BitmapCanvasProvider = new BitmapCanvasProvider(output, mimeType, ImageResolution,
        BufferedImage.TYPE_BYTE_BINARY, false, 0)
    
    val barcode = new EAN13Bean()
    barcode.generateBarcode(canvas, String valueOf ean)
    canvas.finish()
    
    output.toByteArray()
  }
  
}

case class RenderImage(ean: Long)
case class RenderResult(image: Try[Array[Byte]])
  
  