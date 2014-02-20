package com.parascal.akka

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Success, Failure}

object Main {
  val actorSystem = ActorSystem("test-system")
  implicit val timeout = Timeout(10 seconds)
  implicit val executionContext = actorSystem.dispatcher
  def main(args: Array[String]) {
    val testActor = try {
      actorSystem.actorOf(Props(classOf[TestActor], "ponggggg!!!"), name = "test-actor")
    } catch {
      case ex: Exception => {
        println("This:\n\t" + 
          """class TestActor(pongMessage: String, defaultArg: Int = 2)""" + "\n" +
          "should be a valid constructor for:\n\t" +
          """Props(classOf[TestActor], "ponggggg!!!")""" + "\n" +
          "But failed with:")
        ex.printStackTrace
        println("It will succeed with:\n\t" + """Props(classOf[TestActor], "ponggggg!!!", 2)""")
        actorSystem.actorOf(Props(classOf[TestActor], "ponggggg!!!", 2), name = "test-actor")
      }
    }
    (testActor ? "ping") onComplete {
      case Success(msg) =>  {
        println(msg)
      }
      case Failure(ex) => ex.printStackTrace
    }
    actorSystem.shutdown
  }
}

class TestActor(pongMessage: String, defaultArg: Int = 2) extends Actor {
  override def receive: Receive = {
    case "ping" => sender ! pongMessage
    case msg => throw new Exception("Unknown pingMessage: " + msg)
  }
}
