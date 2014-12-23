/**
 * @package   Jdbc
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 */

package jdbc

import java.sql.{Connection,DriverManager}
import com.mysql.jdbc

class Mysql(host: String, database: String, username: String, password: String) {
    val driver = "com.mysql.jdbc.Driver"
    val url    = s"jdbc:mysql://$host:3306/$database"   
    var connection:Connection = _
    try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
    } catch {
        case e: Exception => e.printStackTrace
    }

    def getConnection(): Connection = connection

    def closeConnection(): Unit = connection.close
}
