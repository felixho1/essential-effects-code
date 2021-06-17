package com.innerproduct.ee.effects

case class MyIO[A](unsafeRun: () => A) {
  def map[B](f: A => B): MyIO[B] = MyIO(() => f(unsafeRun()))
  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unsafeRun()).unsafeRun())
} // <1>

object MyIO {
  def putStr(s: => String): MyIO[Unit] =
    MyIO(() => println(s)) // <2>
}

object Printing extends App { // <3>
  val hello = MyIO.putStr("hello!")

  hello.unsafeRun()
}
