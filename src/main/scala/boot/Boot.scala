/**
 * @package   Boot
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 *
 */

package boot

import scalaj.http._
import scala.io.Source

object Boot {
	
	def main(args: Array[String]) {
		println("Iniciando a leitura dos arquivos")
		for (line <- Source.fromFile("sources.txt").getLines()) {
			val result = request(line)
			println(result)
		}
  	}

  	def request(url: String): String = {
  		Http(url).option(HttpOptions.connTimeout(1000)).option(HttpOptions.readTimeout(5000)).asString
  	}
}