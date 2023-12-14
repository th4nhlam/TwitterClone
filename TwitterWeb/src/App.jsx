import { useState } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Login from "./pages/Login/Login";
import "./App.css";
import Register from "./pages/Register/Register";
import TweetList from "./pages/TweetList/TweetList";
import UserContextProvider from "../contexts/userContext";
import AddTweet from "./pages/AddTweet/AddTweet";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Hello world!</div>,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/home",
    element: <TweetList />,
  },
  {
    path: "/add-tweet",
    element: <AddTweet />,
  },
]);

function App() {
  return (
    <UserContextProvider>
      <RouterProvider router={router} />
    </UserContextProvider>
  );
}

export default App;
