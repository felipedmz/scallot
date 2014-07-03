/**
 * @package   Boot
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 *
 */

package boot

import scalaj.http.Http

object Boot {
	var request: Http.Request = Http("http://google.com/")

	def main(args : Array[String]) {
		val result = request.asString
		println(result)		
  	}
    
}