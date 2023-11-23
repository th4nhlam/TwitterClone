<?php

require("DBInfo.inc");

//Call service to register
 // define quesry  //StartFrom
if ( $_GET['op']==1) { // my following
	 
$query="select * from user_tweets where user_id in (select following_user_id from following where user_id=". $_GET['user_id'] . ") or user_id=" . $_GET['user_id'] . " order by tweet_date DESC". 
" LIMIT 20 OFFSET ". $_GET['StartFrom']  ;  // $usename=$_GET['username'];
}

elseif ( $_GET['op']==2) { // specific person post
 
$query="select * from user_tweets where user_id=" . $_GET['user_id'] . " order by tweet_date DESC" . 
" LIMIT 20 OFFSET ". $_GET['StartFrom'] ;  // $usename=$_GET['username'];
}
elseif ($_GET['op']==3) {


$query ="select * from user_tweets where tweet_text like  '%". $_GET['query'] ."%' LIMIT 20 OFFSET ". $_GET['StartFrom'] ;

}
 
 
$result= mysqli_query($connect,$query);

if (!$result){
	die(' Error cannot run query');
}

$userTweets=array();
while ($row= mysqli_fetch_assoc($result)) {
	
	$userTweets[]= $row ;
 
}

if ($userTweets) {
print("{'msg':'has tweet','info':'". json_encode($userTweets) ."'}");
}else{
print("{'msg':'no tweets' }");
}

 mysqli_free_result($result);
mysqli_close($connect);
?>