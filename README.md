# Copy-Cat
An Android application which provides a user with three basic functionalities whenever he/she copies a text. These functionalities are as follows:-
1. A web search of the given text.
2. Meaning of the word  (dictionary.com).
3. Translation of the word to any language using Google Translate. 



Project Details: 
1.Custom View implementation of an exploding button, once tapped provides child views with the above described functionalities. 
2.Used Window Manager for an Overlay that appears on the screen only when a user has copied something. Also the Overlay will automatically disappear within 5 seconds, giving user adequate time to perform an action.
3.Gracefully handling Foreground and Background Services based on the user’s SDK version. 
4.The Service will automatically restart once the phone is turned on after being switched off (or restarted). This will based on user’s prior permission to start the Service.
