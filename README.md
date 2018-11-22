# Copy Cat

### Overview

An Android application which provides user with three basic functionalities to choose from whenever a text is copied.

***Fuctionalities***
- Web search of the given text.
- Meaning of the text using dictionary.com.
- Translation of the word to any language using Google Translate. 


### Project Details 

- Custom View implementation of an exploding button, once tapped provides three child views with the above described functionalities.
- Window Manager is used for an Overlay that appears on the screen only when a user has copied something.
- Services are used to subscribe to the Clipboard events. The application gracefully handles Foreground and Background Services based on the device’s SDK version.
- The application will start the Service once the device is turned on or restarted, this will be based on user’s preference for the application.


### Technologies Used

- Kotlin
