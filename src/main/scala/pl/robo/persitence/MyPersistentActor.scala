package pl.robo.persitence

import akka.actor.{Actor, ActorSelection}
import akka.persistence.{AtLeastOnceDelivery, PersistentActor}

case class Msg(deliveryId: Long, s: String)
case class Confirm(deliveryId: Long)

sealed trait Evt
case class MsgSent(s: String) extends Evt
case class MsgConfirmed(deliveryId: Long) extends Evt

class MyPersistentActor(destination: ActorSelection) extends PersistentActor with AtLeastOnceDelivery {

  override def persistenceId: String = "persistence-id"

  override def receiveCommand: Receive = {
    case s: String =>           persist(MsgSent(s))(updateState)
    case Confirm(deliveryId) => persist(MsgConfirmed(deliveryId))(updateState)
  }

  override def receiveRecover: Receive = {
    case evt: Evt => updateState(evt)
  }

  def updateState(evt: Evt): Unit = evt match {
    case MsgSent(s) =>                deliver(destination)(deliveryId => Msg(deliveryId, s))
    case MsgConfirmed(deliveryId) =>  confirmDelivery(deliveryId)
  }
}

class MyDestination extends Actor {

  override def receive: Actor.Receive = {
    case Msg(deliveryId, s) =>
      // ......
    sender() ! Confirm(deliveryId)
  }
}

