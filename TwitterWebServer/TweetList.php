<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Access-Control-Allow-Methods: GET");

require("DBInfo.inc");

// Define query based on the operation
if ($_GET['op'] == 1) { // My following
    $query = "SELECT * FROM user_tweets WHERE user_id IN (SELECT following_user_id FROM following WHERE user_id=" . $_GET['user_id'] . ") OR user_id=" . $_GET['user_id'] . " ORDER BY tweet_date DESC LIMIT 20 OFFSET " . $_GET['StartFrom'];
} elseif ($_GET['op'] == 2) { // Specific person's posts
    $query = "SELECT * FROM user_tweets WHERE user_id=" . $_GET['user_id'] . " ORDER BY tweet_date DESC LIMIT 20 OFFSET " . $_GET['StartFrom'];
} elseif ($_GET['op'] == 3) { // Search by query
    $query = "SELECT * FROM user_tweets WHERE tweet_text LIKE '%" . $_GET['query'] . "%' LIMIT 20 OFFSET " . $_GET['StartFrom'];
}

$result = mysqli_query($connect, $query);

if (!$result) {
    $output = array('msg' => 'error', 'error' => 'Cannot run query');
} else {
    $userTweets = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $userTweets[] = $row;
    }

    if ($userTweets) {
        $output = array('msg' => 'has tweets', 'info' => $userTweets);
    } else {
        $output = array('msg' => 'no tweets');
    }
}

// Set the content type and encode the response as JSON
header('Content-Type: application/json');
echo json_encode($output);

mysqli_free_result($result);
mysqli_close($connect);
?>
