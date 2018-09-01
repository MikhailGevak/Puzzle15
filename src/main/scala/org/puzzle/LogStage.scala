package org.puzzle

import akka.stream._
import akka.stream.scaladsl.Flow
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

object LogStage {
  def apply[T](name: String, printValue: Boolean = false) = Flow.fromGraph(new LogStage[T](name, printValue))
}

class LogStage[A](name: String, printValue: Boolean) extends GraphStage[FlowShape[A, A]] {
  override val shape = FlowShape.of(in, out)
  val in = Inlet[A]("Log.in")
  val out = Outlet[A]("Log.out")

  override def createLogic(attr: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        val v = grab(in)
        println(s"$name: pushing ${Some(v).filter { _ => printValue }.getOrElse("")}...")
        push(out, v)
      }

      override def onUpstreamFinish(): Unit = {
        println(s"$name: upstream finishing....")
        super.onUpstreamFinish()
      }

      override def onUpstreamFailure(ex: Throwable): Unit = {
        println(s"$name: upstream failed: $ex")
        ex.printStackTrace()
        super.onUpstreamFailure(ex)

      }
    })
    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        println(s"$name: pulling inlet...")
        pull(in)
      }

      override def onDownstreamFinish: Unit = {
        println(s"$name: downstream finishing....")
        super.onDownstreamFinish()
      }
    })
  }
}