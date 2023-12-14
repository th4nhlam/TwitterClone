# Twitter Project

This project is a Twitter-like social media platform developed with a multi-tier architecture, consisting of a Front-end built with ReactJS, a Mobile application developed with Kotlin, and a Back-end powered by PHP.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Folder Structure](#folder-structure)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Twitter project aims to provide users with a social media platform where they can post tweets, follow other users, and interact with content. The project is divided into three main components: Front-end (ReactJS), Mobile (Kotlin), and Back-end (PHP).

## Features

- **Tweet Feed:** Users can view a feed of tweets from themselves and the users they are following.
- **Tweet Creation:** Users can create and post tweets with text and optional images.
- **User Profile:** Users have individual profiles showcasing their tweets and personal information.
- **Mobile Access:** Android mobile application allows users to access the platform on the go.

## Technologies Used

### Front-end (ReactJS)

- ReactJS
- React Router for navigation
- Axios for API requests
- React Icons for iconography
- CSS for styling

### Mobile (Kotlin)

- Kotlin programming language
- Android Studio IDE
- Retrofit for handling API requests
- Picasso for image loading
- Material Design components for UI

### Back-end (PHP)

- PHP for server-side scripting
- MySQL for database management
- RESTful API for communication between the Front-end/Mobile and the Back-end
- CORS headers for handling cross-origin requests

## Folder Structure

- **front-end:** Contains the ReactJS Front-end code.
- **mobile:** Includes the Kotlin-based Android mobile application code.
- **back-end:** Contains the PHP Back-end code.
- **database:** SQL scripts for database setup and management.

## Getting Started

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-username/twitter-project.git
   cd twitter-project
   ```

2. **Setup Database:**

   - Execute the SQL scripts in the `database` folder to set up the required database and tables.

3. **Front-end Setup:**

   ```bash
   cd front-end
   npm install
   npm start
   ```

4. **Mobile Setup:**

   - Open the `mobile` project in Android Studio.
   - Ensure you have the necessary dependencies installed.
   - Run the application on an emulator or a physical Android device.

5. **Back-end Setup:**

   - Configure the PHP server to host the Back-end.
   - Update the database connection details in the Back-end code.

6. **Access the Application:**

   - Open the Front-end in a web browser (http://localhost:3000 by default).
   - Launch the mobile application on an Android device.

## Contributing

Feel free to contribute to the development of this project by submitting pull requests or reporting issues.

## License

This project is licensed under the [MIT License](LICENSE).
