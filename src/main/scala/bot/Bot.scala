/**
 * @package   Boot
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 *
 * Commented code for pedagogical purposes :D
 */

package bot

import scalaj.http._    // it's a java lib wrapped
import scala.io.Source  // to read physical files

object Bot {
	var ways: List[String] = List()
	var embeds: List[String] = List()

	def main(args: Array[String]) {
		println("\nInitialized reading of primary sources")
		loadPrimarySources

		println("\nSearching for compatible embeds")
		embeds = searchForEmbeds(ways)
  	}

  	def searchForEmbeds(urls: List[String]): List[String] = {
  		for (url <- ways) { // this is like a foreach
  			try {
		  		val html  = getRequest(url)
		  		val embed = """<embed(.)*>(.)*</embed>""".r // this is a Regex
				val found = (embed findAllIn html).toList   // syntax sugar for "embed.findAllIn(html)"
		  		for (e <- found) {
					println(e)
					println("\n")
				}
			} catch {
				case error: Exception => println(s"$error >>> $url \n")
			}
		}
  		urls // the last line is the return
  	}

  	def loadPrimarySources(): Unit = { // Unit is a Scala's 'void'
  		for (line <- Source.fromFile("sources.txt").getLines()) {
			val result = getRequest(line)
			findLinks(result)
		}
  	}

  	def findLinks(html: String): Unit = {
  		val link  = """http://[a-z0-9./?!@#$%&*)(-_=+;:,\|^~}{]*""".r
		val found = (link findAllIn html).toList
		ways = ways union found // there is a sum of lists (Yes, easy as you see ;D)
  	}

  	def getRequest(url: String): String = {
  		/* How Http is a wrapped lib, this sintax sounds like Java, 
  		   with the function returning directly the last line */
  		Http(url)
  			.option(HttpOptions.connTimeout(2000))
  			.option(HttpOptions.readTimeout(5000))
  			.header("User-Agent", "Opera")
  			.asString
  	}
}
