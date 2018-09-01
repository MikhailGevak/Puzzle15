package org.puzzle.app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.puzzle.flow.GameGraph
import org.puzzle.flow.impl.ConsoleGraph

import scala.concurrent.ExecutionContext

object GameApp extends App {
  implicit val system = ActorSystem("Puzzle")
  implicit val mat = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val gameDimension = system.settings.config.getInt("game.dimension")

  require(gameDimension > 1, "Game Field's dimension can be more than 1")
  val consoleGraph = new ConsoleGraph(Console.in, Console.out)

  GameGraph(consoleGraph).game(gameDimension).run.onComplete { _ => system.terminate() }
}
