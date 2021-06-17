package com.innerproduct.ee.io

import cats.effect._
import com.innerproduct.ee.debug._

import scala.concurrent.duration._

object TickingClock extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    tickingClock.as(ExitCode.Success)

  val tickingClock: IO[Unit] = {
    for {
//      _ <- IO(println(System.currentTimeMillis))
      _ <- IO(System.currentTimeMillis).debug
      _ <- IO.sleep(1.second)
      _ <- tickingClock
    } yield ()
  }
  // <1>
}
