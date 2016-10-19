package pl.robo.supervision

import akka.Done
import akka.actor.Actor

class WorkerActor extends Actor {

  override def receive: Receive = {
    case i: Int =>
      sender() ! i + 1
    case message: Any =>
      println(s"Received $message")
      sender() ! Done
  }

}

