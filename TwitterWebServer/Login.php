<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Access-Control-Allow-Methods: GET");

require("DBInfo.inc");

$email = mysqli_real_escape_string($connect, $_GET['email']);
$password = mysqli_real_escape_string($connect, $_GET['password']);

$query = "SELECT * FROM login WHERE email='$email' AND password='$password'";
$result = mysqli_query($connect, $query);

if (!$result) {
    die('Error: Cannot run query');
}

$userInfo = array();
while ($row = mysqli_fetch_assoc($result)) {
    $userInfo[] = $row;
    break; // to be safe
}

if ($userInfo) {
    $response = array('msg' => 'pass login', 'info' => $userInfo);
    echo json_encode($response);
} else {
    echo json_encode(array('msg' => 'cannot login'));
}

mysqli_free_result($result);
mysqli_close($connect);
?>