package cats101

import cats._
import cats.data._
import cats.implicits._
import cats.instances.list._

object Comonads101 extends App {
  val lst = List(1, 2).coflatMap(identity)
  lst.foreach(println)
}
