/**
 * @package   Boot
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
		println("\nReading primary sources")
		loadPrimarySources

		println("\nSearching for embeds")
		searchForEmbeds(ways)

		save(embeds)
  	}

  	def save(videos: List[String]): Unit = {
  		val db = new Mysql("localhost", "test", "root", "test")
  		val conn = db.getConnection()
  		try {
	  		for (video <- videos) {
	  			val statement = conn.prepareStatement("INSERT INTO videos (embed) VALUES (?)")
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
				case error: Exception => println(s"$error >>> $url \n")
			}
		}
  	}

  	def loadPrimarySources(): Unit = {
  		for (line <- Source.fromFile("sources.txt").getLines()) {
			val result = getRequest(line)
			findLinks(result)
		}
  	}

  	def findLinks(html: String): Unit = {
  		val link  = """http://[a-z0-9./?!@#$%&*)(-_=+;:,\|^~}{]*""".r
		val found = (link findAllIn html).toList
		ways = ways union found
  	}

  	def getRequest(url: String): String = {
  		Http(url)
  			.option(HttpOptions.connTimeout(2000))
  			.option(HttpOptions.readTimeout(5000))
  			.header("User-Agent", "Opera")
  			.asString
  	}
}
