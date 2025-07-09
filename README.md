# MovieApp 

MovieApp is a modern Android application designed for discovering, browsing, and rating movies. It offers personalized recommendations, user authentication, and rich movie details through integration with a backend service.

---

## Features

- Firebase Authentication (Google Sign-In)
- Browse trending, popular, and personalized recommended movies
- Search movies 
- View detailed movie information with images and metadata
- Rate movies with 1 to 5 stars
- Add movies to favorites
- Jetpack Compose-based clean and responsive UI
- Dependency Injection (Hilt)
- MVVM architecture with Kotlin Coroutines and Flow
- Retrofit: Firebase ID token added to all requests for secure communication
- Fully **asynchronous** architecture using Kotlin Coroutines

---

## 🛠️ Tech Stack

| Layer            | Technology                                   |
|------------------|----------------------------------------------|
| Language         | Kotlin                                       |
| UI               | Jetpack Compose                              |
| Architecture     | MVVM + Repository Pattern                    |
| Networking       | Retrofit + OkHttp                            |
| Auth             | Firebase Authentication (Google Sign-In)     |
| DI               | Hilt                                         |
| Concurrency      | Kotlin Coroutines, Flows                     |
| Backend API      | [Spring Boot (REST API)](https://github.com/Onurege00/MovieApp-backend)                       |
| Database (via API) | Neo4j Graph DB                             |
| Image Loading    | coil                                         |


---

## Usage

- Launch the app and sign in using Google Sign-In.
- Browse movies on the home screen or search by title.
- Tap any movie to see details, rate it, or add it to favorites.
- Explore personalized recommendations based on your ratings.

---
