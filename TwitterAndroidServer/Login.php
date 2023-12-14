<?php

// Allow requests from any origin
header("Access-Control-Allow-Origin: *");

// Allow the content type to be sent with the request
header("Access-Control-Allow-Headers: Content-Type");

// Allow GET requests
header("Access-Control-Allow-Methods: GET");

require("DBInfo.inc");

$query ="select * from login  where email='" . $_GET['email'] ."' and password='" . $_GET['password'] ."'" ;
$result= mysqli_query($connect,$query);

if (!$result){
	die(' Error cannot run query');
}

$userInfo=array();
while ($row= mysqli_fetch_assoc($result)) {
	
	$userInfo[]= $row ;
	break; // to be save
}

if ($userInfo) {
print("{'msg':'pass login','info':'". json_encode($userInfo) ."'}");
}else{
print("{'msg':'cannot login' }");
}

 mysqli_free_result($result);
mysqli_close($connect);
?>