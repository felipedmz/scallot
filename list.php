<?php
/**
 * @author    Felipe Maziero <flpmaziero@gmail.com>
 * @copyright 2014 Felipe Maziero
 * 
 * Just a list to validate saved embeds
 */

ini_set('display_errors', E_ALL);
$pdo    = new PDO("mysql:host=localhost;dbname=scallot", "root", "test");
$result = $pdo->query("SELECT * FROM video");
?>

<?php foreach ($result as $row): ?>
	<div>
		&nbsp;<?php echo $row['embed_html']; ?>
	</div>
<?php endforeach; ?>
