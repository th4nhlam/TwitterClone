<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Access-Control-Allow-Methods: GET");

require("DBInfo.inc");

// Check if all required parameters are present
if (
    isset($_GET['user_id']) &&
    isset($_GET['tweet_text']) &&
    isset($_GET['tweet_picture'])
) {
    // Sanitize input to prevent SQL injection
    $userId = mysqli_real_escape_string($connect, $_GET['user_id']);
    $tweetText = mysqli_real_escape_string($connect, $_GET['tweet_text']);
    $tweetPicture = mysqli_real_escape_string($connect, $_GET['tweet_picture']);

    // Use prepared statement to prevent SQL injection
    $query = "INSERT INTO tweets (user_id, tweet_text, tweet_picture) VALUES (?, ?, ?)";
    $stmt = mysqli_prepare($connect, $query);

    if ($stmt) {
        mysqli_stmt_bind_param($stmt, "iss", $userId, $tweetText, $tweetPicture);
        $result = mysqli_stmt_execute($stmt);

        if ($result) {
            $output = json_encode(array('msg' => 'tweet is added'));
        } else {
            $output = json_encode(array('msg' => 'fail'));
        }

        mysqli_stmt_close($stmt);
    } else {
        $output = json_encode(array('msg' => 'fail'));
    }
} else {
    $output = json_encode(array('msg' => 'fail', 'error' => 'Missing required parameters'));
}

// Set the content type and encode the response as JSON
header('Content-Type: application/json');
echo $output;

mysqli_close($connect);
?>