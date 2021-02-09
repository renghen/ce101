import cats.effect._
import cats.syntax.all._

import scala.concurrent.duration._
import cats.effect.std.Random
import cats.effect.kernel.Fiber
import java.util.concurrent.Future

object HelloCats extends IOApp.Simple {

  val randomIO = Random.scalaUtilRandom[IO]

  def greeter(word: String): IO[Unit] =
    for {
      random <- randomIO
      duration <- random.nextIntBounded(10)
      _ <- (IO.println(word) >> IO.sleep(duration.millis)).foreverM
    } yield ()

  override def run =
    for {
      hello <- greeter("Hello").start
      world <- greeter("world").start
      _ <- IO.sleep(5.seconds)
      _ <- hello.cancel >> world.cancel
    } yield ()
}
