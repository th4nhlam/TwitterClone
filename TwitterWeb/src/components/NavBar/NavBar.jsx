import React, { useContext } from "react";
import { FaHome, FaPlus, FaSignOutAlt } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../../contexts/userContext";
import "./NavBar.css";

const NavBar = () => {
  const navigate = useNavigate();

  const { setUser } = useContext(UserContext);

  const handleHome = () => {
    navigate("/home");
  };

  const handleAddTweet = () => {
    navigate("/add-tweet");
  };

  const handleLogout = () => {
    setUser(null);
    navigate("/login");
  };

  return (
    <nav className="bottom-navigation">
      <button onClick={handleHome}>
        <FaHome /> Home
      </button>
      <button onClick={handleAddTweet}>
        <FaPlus /> Add Tweet
      </button>
      <button onClick={handleLogout}>
        <FaSignOutAlt /> Logout
      </button>
    </nav>
  );
};

export default NavBar;
