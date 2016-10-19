package pl.robo.supervision

import akka.Done
import akka.actor.SupervisorStrategy._
import akka.actor._

import scala.concurrent.duration.DurationInt


case object Join


class MasterActor(n: Int) extends Actor {

   override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute){
     case  _ : ArithmeticException => Resume
     case  _ : NullPointerException => Restart
     case  _ : IllegalArgumentException => Stop
     case  _ : Exception => Escalate
   }

  val worker = context.actorOf(Props[WorkerActor])
  val workerNotChildren = context.system. actorOf(Props[WorkerActor])

  def waiting(i: Int, l: List[ActorRef]): Receive = {
    case Join =>
      if (i > 1) {
        context.become(waiting(i - 1, sender() :: l))
      } else {
        context.become(working(sender() :: l))
      }
  }

  def working(workers: List[ActorRef]): Receive = {
    case Done =>
      println("Work is done")
      if (workers.isEmpty) {
        context.become(waiting(10, List.empty[ActorRef]))
      } else {
        context.become(working(workers.filterNot(sender() == _)))
      }
    case message: Any =>
      println(s"Sending msg $message to worker actor")
      workers.head forward message
  }

  override def receive: Receive = waiting(10, List.empty[ActorRef])

}

