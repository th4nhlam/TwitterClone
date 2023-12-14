import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../../contexts/userContext";
import "./Login.css";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const { user, setUser } = useContext(UserContext);

  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      navigate("/home");
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `http://localhost:8080/TwitterWebServer/Login.php?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const responseData = await response.json();
      console.log(responseData);

      if (responseData.msg === "pass login") {
        // Successful authentication, you can redirect the user or perform other actions
        console.log("Authentication successful");
        setUser(responseData.info[0]);
        navigate("/home");
      } else {
        // Handle authentication failure
        console.error("Authentication failed");
      }
    } catch (error) {
      console.error("Error during authentication:", error);
    }
  };

  return (
    <div className="mobile-login-container">
      <h2>Login to Twitter</h2>
      <form className="mobile-login-form" onSubmit={handleSubmit}>
        <label>
          Email:
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </label>
        <label>
          Password:
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </label>
        <button type="submit">Login</button>
      </form>
      <p className="signup-link">
        Don't have an account? <a href="/register">Sign up</a>
      </p>
    </div>
  );
};

export default Login;
