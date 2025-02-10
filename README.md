# MediaScout 🎥

Welcome to **MediaScout**, a Spring Boot-powered web application designed to help you rediscover lost media from your childhood! MediaScout uses AI and APIs to help you find it. 

## 🚀 About the Project

MediaScout is a learning project focused on exploring **Spring Boot** and working with **APIs**. The goal was to build a functional web application that leverages AI (via OpenAI) and image search (via Google Custom Search) to help users find media based on specific criteria like time period, type of media and custom descriptions.

### Key Features:
- **AI Media Suggestions**: Media recommendations are based on your input using OpenAI's gpt-4o-mini. 
- **Image Search**: The app finds relevant images for the suggested media using Google Custom Search. 
- **User-Friendly Interface**: Built with Bootstrap for a clean and responsive UI. 

## 🛠️ Tech Stack

### Backend:
- **Spring Boot**: The backbone of the application, handling API requests. 🍃
- **OpenAI API**: Powers the AI-driven media suggestions. 🧠
- **Google Custom Search API**: Fetches images for the suggested media. 🔍

### Frontend:
- **HTML/CSS**: For structuring and styling the web pages. 🖌️
- **JavaScript**: Handles dynamic content and API calls using the Fetch API. 🕸️
- **Bootstrap**: Ensures a responsive and modern user interface. 🎯

## 🧑‍💻 How It Works

1. **User Input**: Enter details like the type of media (movie, book, music), time period, genre, and any additional descriptions.
2. **AI Suggestion**: The app sends your input to OpenAI's gpt-4o-mini, which generates a media suggestion.
3. **Image Fetching**: The app then uses Google Custom Search to find an image related to the suggested media.
4. **Display Results**: The suggested media and its image are displayed on the screen.
