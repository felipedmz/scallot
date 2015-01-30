/**
 * @package   Bot
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 */

package bot

import scalaj.http._
import scala.io.Source
import jdbc.Mysql

object Bot {
	var ways: List[String] = List()
	var embeds: List[String] = List()

	def main(args: Array[String]) {
		println("\nReading primary sources\n")
		loadPrimarySources

		println("\nSearching for embeds\n")
		searchForEmbeds(ways)

		val totalFound = embeds.count(_ != "")
		println(s"\n$totalFound items found")

		if (totalFound > 0) {
			println("\nSaving in MySQL database")
			save(embeds)	
		}
		
		println("\nFinished")
  	}

  	def save(videos: List[String]): Unit = {
  		val db = new Mysql("localhost", "scallot", "root", "test")
  		val conn = db.getConnection()
  		try {
	  		for (video <- videos) {
	  			val statement = conn.prepareStatement("INSERT INTO video (embed_html) VALUES (?)")
	    		statement.setString(1, video)
	    		statement.executeUpdate
	  		}
	  	} catch {
	  		case error: Exception => error.printStackTrace
	  	} finally {
   			db.closeConnection()
  		}
  	}

  	def searchForEmbeds(urls: List[String]): Unit= {
  		for (url <- ways) {
  			try {
		  		val html  = getRequest(url)
		  		val embed = """<embed(.)*>(.)*</embed>""".r
				val found = (embed findAllIn html).toList
		  		for (e <- found) {
					embeds = embeds :+ e
				}
			} catch {
				case error: Exception => println(s"$error on $url")
			}
		}
  	}

  	def loadPrimarySources(): Unit = {
  		for (line <- Source.fromFile("sources.txt").getLines()) {
			val result = getRequest(line)
			findLinks(line, result)
		}
  	}

  	def findLinks(baseUri: String, html: String): Unit = {
  		val links  = """href=["](https?:/)?/[a-z0-9./?!@#$%&*)(-_=+;:,\|^~}{]*["]""".r
		val found = (links findAllIn html).toList
		for (i <- found) {
			var link = i
			link = link.replaceAllLiterally("href=", "").replaceAllLiterally("\"", "")
			if (link.indexOf("http") < 0) {
				link = baseUri + link.substring(1)
			}
			ways = ways :+ link
		}
  	}

  	def getRequest(url: String): String = {
  		println(s"Looking at: $url")
  		Http(url)
  			.option(HttpOptions.connTimeout(2000))
  			.option(HttpOptions.readTimeout(5000))
  			.header("User-Agent", "Opera")
  			.asString
  	}
}
