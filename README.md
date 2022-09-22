# Page
GitHub Api with paging3 library, MVVM, Room, Tests, etc

## Tech stack & Third-party libraries
- Architecture
  - Multi-modular application
  - MVVM Architecture
- [Kotlin](https://kotlinlang.org/), [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations and background processes.
- Jetpack
  - Lifecycle - Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel - Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - Room - Constructs Database by providing an abstraction layer over SQLite to allow fluent database access
  - Hilt - Manage dependency injection.
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Paging library helps you load and display pages of data from a larger dataset from local storage or over network. 
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.
- [Moshi](https://github.com/square/moshi/) - A modern JSON library for Kotlin and Java.
- [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components for building ripple animation, and CardView.
- Testing
  - JUnit4 and Coroutines Test
  - [Truth](https://truth.dev/) - Truth is a library for performing assertions in tests 
  - [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - A scriptable web server for testing HTTP clients 
