import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../../contexts/userContext";
import NavBar from "../../components/NavBar/NavBar";
import "./AddTweet.css";

const AddTweet = () => {
  const navigate = useNavigate();
  const { user } = useContext(UserContext); // Use your actual UserContext
  const [tweetText, setTweetText] = useState("");
  const [tweetPicture, setTweetPicture] = useState("");
  const [error, setError] = useState("");

  const handleAddTweet = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/TwitterWebServer/TweetAdd.php?user_id=${user.user_id}&tweet_text=${encodeURIComponent(
          tweetText
        )}&tweet_picture=${encodeURIComponent(tweetPicture)}`
      );

      if (response.ok) {
        const responseData = await response.json();
        console.log(responseData);
        if (responseData.msg === "tweet is added") {
          // Redirect to the home page after a successful tweet addition
          navigate("/home");
        } else {
          setError("Failed to add tweet");
        }
      } else {
        setError("Failed to add tweet");
      }
    } catch (error) {
      console.error("Error adding tweet:", error);
      setError("Failed to add tweet");
    }
  };

  return (
    <div className="mobile-add-tweet-container">
      <header>
        <h1>Add Tweet</h1>
      </header>
      <main>
        <textarea placeholder="What's happening?" value={tweetText} onChange={(e) => setTweetText(e.target.value)}></textarea>
        <input
          type="text"
          placeholder="Optional: Add an image URL"
          value={tweetPicture}
          onChange={(e) => setTweetPicture(e.target.value)}
        />
        {error && <p className="error-message">{error}</p>}
        <button onClick={handleAddTweet}>Tweet</button>
      </main>
      <NavBar />
    </div>
  );
};

export default AddTweet;
