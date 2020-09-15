# AND_p3_flick_pick
## Udacity Android Developer Nanodegree project 3 


## Project Overview
This is stage II of the Popular Movies project. In this stage I added additional functionality to the core movie app by adding Room for offline caching and MVVM architecture. Users can also view and play trailers on YouTube, read reviews of a selected movie and mark movies as favorite to be stored in a local database. 


## Screenshots
![Screen](https://raw.githubusercontent.com/kangarruu/AND_p3_flick_pick/master/flickpick_screenshot.png)

## Getting Started
In order to build the app you must provide your own API key from [The Movie Database (TMDb)](https://www.themoviedb.org/documentation/api) API. Please request an [API Key](https://www.themoviedb.org/documentation/api) and enter it into the gradle.properties file:

```gradle.properties
MOVIE_DB_API_KEY="Your Api Key"
```


## Features
*   Fetch data from the Internet with theMovieDB API.
*   Use Recyclerview to present the user with a grid arrangement of movie posters upon launch.
*   Allow the user to change sort order by most popular, highest-rated, or favorites
*   Allow the user to tap on a movie poster and transition to a details screen with additional information such as plot synopsis, user rating, release date, view and play trailers on youtube, and read reviews
*   Allow the user to mark a movie as favorite by tapping a star icon 
*   Offline support: app makes use of Room for offline caching
*   MVVM with Android Architecture Components(Room, LiveData, ViewModel)
*   Handle network status and network failures
*   ConstraintLayout

## Libraries used
*   [AndroidX](https://developer.android.com/jetpack/androidx/)
*   [Picasso](http://square.github.io/picasso/)
*   [Retrofit 2](https://github.com/square/retrofit)
*   [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
*   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
*   [Room](https://developer.android.com/jetpack/androidx/releases/room)

