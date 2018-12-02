# Copy Cat

### Overview

An Android application which provides the following functionalities to choose from whenever a text is copied anywhere on the device:

- Web search of the given text
- Meaning of the text using dictionary.com
- Translation of the text to any language using Google Translate


### Project Details 

- Custom View implementation of an exploding button, when tapped animates into three more buttons with the above functionalities 
- Window Manager is used to display an Overlay that appears whenever a user copies something
- The application gracefully selects Foreground or Background Service based on the device's SDK version which is used to subscribe to the clipboard events.
- A feature is available which lets the Service stay on/off when the device starts or restarts based on user preference


### Technologies Used

- Kotlin
