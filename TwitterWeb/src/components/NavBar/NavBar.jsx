import { FaHome, FaPlus } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import "./NavBar.css";

const NavBar = () => {
  const navigate = useNavigate();

  const handleHome = () => {
    navigate("/home");
  };

  const handleAddTweet = () => {
    navigate("/add-tweet");
  };

  return (
    <nav className="bottom-navigation">
      <button onClick={handleHome}>
        <FaHome /> Home
      </button>
      <button onClick={handleAddTweet}>
        <FaPlus /> Add Tweet
      </button>
    </nav>
  );
};

export default NavBar;
