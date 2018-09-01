package org.puzzle

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

  val consoleGraph = new ConsoleGraph(Console.in, Console.out)

  GameGraph(consoleGraph).game(gameDimension).run.onComplete { _ => system.terminate() }
}
