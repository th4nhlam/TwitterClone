<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Access-Control-Allow-Methods: GET");

require("DBInfo.inc");

// Check if all required parameters are present
if (
    isset($_GET['first_name']) &&
    isset($_GET['email']) &&
    isset($_GET['password']) &&
    isset($_GET['picture_path'])
) {
    // Sanitize input to prevent SQL injection
    $firstName = mysqli_real_escape_string($connect, $_GET['first_name']);
    $email = mysqli_real_escape_string($connect, $_GET['email']);
    $password = mysqli_real_escape_string($connect, $_GET['password']);
    $picturePath = mysqli_real_escape_string($connect, $_GET['picture_path']);

    // Use prepared statement to prevent SQL injection
    $query = "INSERT INTO login (first_name, email, password, picture_path) VALUES (?, ?, ?, ?)";
    $stmt = mysqli_prepare($connect, $query);

    if ($stmt) {
        mysqli_stmt_bind_param($stmt, "ssss", $firstName, $email, $password, $picturePath);
        $result = mysqli_stmt_execute($stmt);

        if ($result) {
            $output = json_encode(array('msg' => 'user is added'));
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

print($output);

mysqli_close($connect);
?>