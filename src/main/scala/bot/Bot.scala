/**
 * @package   Boot
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 *
 */

package bot

import scalaj.http._
import scala.io.Source

object Bot {
	var ways: List[String] = List()
	var embeds: List[String] = List()

	def main(args: Array[String]) {
		println("\nIniciando a leitura das fontes primarias")
		loadPrimarySources

		println("\nBuscando por embeds compatíveis")
		embeds = searchForEmbeds(ways)
  	}

  	def searchForEmbeds(urls: List[String]): List[String] = {
  		println("\n@TODO searchForEmbeds")
  		urls
  	}

  	def loadPrimarySources(): Unit = {
  		for (line <- Source.fromFile("sources.txt").getLines()) {
			val result = getRequest(line)
			findLinks(result)
		}
  	}

  	def findLinks(html: String): Unit = {
  		val link   = """http://[a-z0-9./?!@#$%&*)(-_=+;:,\|^~}{]*""".r
		val found  = (link findAllIn html).toList
		ways = ways union found
  	}

  	def getRequest(url: String): String = {
  		Http(url).option(HttpOptions.connTimeout(2000)).header("User-Agent", "Opera").asString
  	}
}