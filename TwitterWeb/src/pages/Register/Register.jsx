import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Register.css";

const Register = () => {
  const [fullName, setFullName] = useState("");
  const [picturePath, setpicturePath] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        `http://localhost:8080/TwitterWebServer/Register.php?first_name=${encodeURIComponent(fullName)}&email=${encodeURIComponent(
          email
        )}&password=${encodeURIComponent(password)}&picture_path=${encodeURIComponent(picturePath)}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        const responseData = await response.json();
        console.log(responseData);
        navigate("/login");
      } else {
        // Handle registration failure
        console.log("Registration failed");
      }
    } catch (error) {
      console.error("Error during registration:", error);
    }
  };

  return (
    <div className="mobile-register-container">
      <h2>Create a new account</h2>
      <form className="mobile-register-form" onSubmit={handleSubmit}>
        <label>
          Full Name:
          <input type="text" value={fullName} onChange={(e) => setFullName(e.target.value)} />
        </label>

        <label>
          Picture path:
          <input type="text" value={picturePath} onChange={(e) => setpicturePath(e.target.value)} />
        </label>

        <label>
          Email:
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </label>

        <label>
          Password:
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </label>

        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};

export default Register;
