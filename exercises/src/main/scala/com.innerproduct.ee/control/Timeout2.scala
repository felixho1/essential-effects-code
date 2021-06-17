package com.innerproduct.ee.control

import cats.effect._
import cats.implicits._
import cats.effect.implicits._
import com.innerproduct.ee.debug._
import scala.concurrent.duration._

object Timeout2 extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      done <- IO.racePair(task, timeout) // <1>
      _ <- done match { // <2>
        case Left((a, fb)) =>
          (IO.pure(a), fb.join).mapN((a, b) => b) // <3>
        case Right((fa, b)) => (fa.join, IO.pure(b)).mapN((a, b) => b) // <4>
      }
    } yield ExitCode.Success

  val task: IO[Unit] = annotatedSleep("   task", 100.millis) // <6>
  val timeout: IO[Unit] = annotatedSleep("timeout", 500.millis)

  def annotatedSleep(name: String, duration: FiniteDuration): IO[Unit] =
    (
      IO(s"$name: starting").debug *>
        IO.sleep(duration) *> // <5>
        IO(s"$name: done").debug
    ).onCancel(IO(s"$name: cancelled").debug.void).void

}
