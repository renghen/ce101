package http4s.client

import org.http4s.client.blaze._
import org.http4s.client._
import scala.concurrent.ExecutionContext.global
import cats.effect._
import cats.syntax.all._
import org.jsoup.parser.Parser
import org.jsoup.Jsoup
import java.util.stream.Collectors
import collection.JavaConverters._
import cats.effect.syntax.resource

import java.io.File
import fs2.io.file.Files
import fs2.Chunk
import java.nio.file.Paths

object OnePiece extends IOApp.Simple {

  val url = "https://w6.readonepiece.online/"

  def getUrls(mainPageIO: IO[String]) = mainPageIO.map { mainPage =>
    Jsoup
      .parse(mainPage)
      .select("#ceo_latest_comics_widget-3 > ul > li > a")
      .eachAttr("href")
      .asScala
      .toList
  }

  def getImagesUrls(episodePage: IO[String]) = episodePage.map {
    Jsoup
      .parse(_)
      .select("div.separator > a > img")
      .eachAttr("src")
      .asScala
      .toList
  }

  def downloadImages(imagesIO: IO[List[String]], client: Client[IO]) = {
    val path = Paths.get("file.jpg")
    fs2.Stream
      .eval(
        client.expect[Chunk[Byte]](
          "https://1.bp.blogspot.com/-6JHEGW1wS6s/YBRu5Gry-hI/AAAAAAAAaqc/y6ea9COkWWMEFpUIOtceEqfvrm3cqusEwCLcBGAsYHQ/s1185/one_piece_1002_2.jpg"
        )
      )
      .flatMap {
        fs2.Stream.chunk(_).through(Files[IO].writeAll(path))
      }
      .compile
      .drain
  }

  override def run =
    BlazeClientBuilder[IO](global).resource.use { client =>
      val responseIO = client.expect[String](url)
      val urlsIO = getUrls(responseIO)
      (urlsIO >>= { urls =>
        urls.parTraverse { episodeUrl =>
          val images = getImagesUrls(client.expect[String](episodeUrl))
          downloadImages(images, client)

        }
      }) *> IO.unit
    }
}
