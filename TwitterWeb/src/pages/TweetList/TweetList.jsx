import React, { useContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../../contexts/userContext";
import NavBar from "../../components/NavBar/NavBar";
import "./TweetList.css";

const TweetList = () => {
  const [tweets, setTweets] = useState([]);
  const [showAllTweets, setShowAllTweets] = useState(true);
  const { user } = useContext(UserContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate("/login");
    } else {
      fetchTweets();
    }
  }, [showAllTweets]);

  const fetchTweets = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/TwitterWebServer/TweetList.php?op=${showAllTweets ? 1 : 2}&user_id=${user.user_id}&StartFrom=0`
      );

      if (response.ok) {
        const responseData = await response.json();
        if (responseData.msg === "has tweets") {
          setTweets(responseData.info);
          console.log("tweet list", responseData.info);
        } else {
          console.log(responseData);
        }
      } else {
        console.error("Error fetching tweets");
      }
    } catch (error) {
      console.error("Error fetching tweets:", error);
    }
  };

  const toggleTweets = () => {
    setShowAllTweets((prev) => !prev);
  };

  return (
    <div className="mobile-home-container">
      <header>
        <h1>Twitter</h1>
      </header>
      <main>
        <label>Show Tweets:</label>
        <select value={showAllTweets ? "all" : "following"} onChange={toggleTweets}>
          <option value="all">All Tweets</option>
          <option value="following">Only My Following Tweets</option>
        </select>
        <div className="tweet-list">
          {tweets?.length > 0 ? (
            tweets.map((tweet) => (
              <div className="tweet" key={tweet.tweet_id}>
                <div className="user-info">
                  <span className="username">@{tweet.first_name}</span>
                </div>
                <p>{tweet.tweet_text}</p>
                {tweet.tweet_picture ? <img height="100px" src={tweet.tweet_picture} alt="Tweet picture" /> : ""}
              </div>
            ))
          ) : (
            <p>No tweets to display.</p>
          )}
        </div>
      </main>
      <NavBar />
    </div>
  );
};

export default TweetList;
