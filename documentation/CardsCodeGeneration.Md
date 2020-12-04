# Cards Code Generation

## Crisis Cards

Crisis Cards are the application's standard way of representating a certain crisis. From the outside, they are simple MaterialDesignCard with a picture and a title. However, with the nature of how dynamic our application is, and the way we need to be constantly adding crisis with different types of contents, a simple work around was thought of to create customizable cards. Another way that allowed us to perform this, was the fact that all our crisis were similar.
Normally, a card would be written in XML, however, these cards are written in Java, and then later added to our views. This simplifies the process of having to create endless cards, as well as give our application dynamicity. Each card is responsible to open a Screen, with all the information of that event retrieved from our Database.

### Generation Process

The process of generating a card uses a bottom-up approach:

- Recognize the inner layers and build them
- Embed these inner layers inside outer layers
- Add the outer layers to the main View (MaterialDesignCard)

## User Cards

User Cards, similar in terms of functionality to a Crisis Cards, are used to represent a User in our Application. Clicking on that card will open the user profile.
