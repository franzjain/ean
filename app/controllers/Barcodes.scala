package com.github.playforscala.barcodes

import play.api.mvc.{ Action, Controller }
import util.{ Failure, Success, Try }

object Barcodes extends Controller {

  val ImageResolution = 144
  
  def barcode(ean: Long) = Action{
    
    import java.lang.IllegalArgumentException
    
    val MimeType = "image/png"
    Try (ean13BarCode(ean, MimeType)) match {
      case Success(imageData) => Ok(imageData).as(MimeType)
      case Failure(e) => BadRequest("couldn't generate barcode: " + e.getMessage())
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