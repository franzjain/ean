package com.github.playforscala.barcodes

import play.api.{ Application, Logger, Plugin }
import akka.actor.Props
import play.api.libs.concurrent.Akka
import play.api.Play.current

class BarcodesPlugin(val app: Application) extends Plugin {

  override def onStart() {
    Logger.info("Initilizing cache")
    Barcodes.barcodeCache = Akka.system.actorOf(Props[BarcodeCache])
  }
  
  override def onStop() {
    Logger.info("Stopping application")
  }
  
  override def enabled = true
}