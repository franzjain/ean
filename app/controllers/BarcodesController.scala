package com.github.playforscala.barcodes

import play.api.mvc.{ Action, Controller }
import scala.util.{ Success, Failure }
import play.api.libs.concurrent.Execution.Implicits._

object BarcodesController extends Controller {

  def barcode(ean: Long) = Action.async {
    Barcodes.renderImage(ean) map {
      case Success(image) => Ok(image).as(Barcodes.MimeType)
      case Failure(e)     => BadRequest(s"Couldn't generate bar code. Error: ${e.getMessage}")
    }
  }
}
