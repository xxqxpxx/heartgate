<?php

	//class db {
//		//properties
//		private $dbhost = "localhost";
//		private $dbuser = "root";
//		private $dbpass = "root1234";
//		private $dbname = "heartgate_pro";
//		
//		//connect
//		public function connect(){
//		
//			$mysql_connect_str = "mysql:host=$this->dbhost;dbname=$this->dbname;";
//			$dbconnection = new PDO($mysql_connect_str, $this->dbuser, $this->dbpass);
//			$dbconnection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
//			return $dbconnection;
//		}
//		
//		
//	}
$db_host = "localhost";
$db_user = "user_pro";
$db_pass = "new@heartgate@2017";
$db_lang = "UTF-8";
$db_name = "heartgate_pro";
date_default_timezone_set('africa/cairo');

$link = @mysql_connect($db_host, $db_user, $db_pass);
if ($link) {
    @mysql_select_db($db_name, $link);
    
	
} else {
    die("Error in connection to database");
}
?>