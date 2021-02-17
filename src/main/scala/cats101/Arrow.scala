package cats101

import cats.arrow.Arrow
import cats.implicits._
import cats.syntax._

object Arrow101 extends App {

  def combine[F[_, _]: Arrow, A, B, C](
      fab: F[A, B],
      fac: F[A, C]
  ): F[A, (B, C)] =
    Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)

  val mean: List[Int] => Double =
    combine((_: List[Int]).sum, (_: List[Int]).size) >>> { case (x, y) =>
      x.toDouble / y
    }

  println(mean(List(1, 2, 3, 4)))

}
