# Cards Generation Feature

## Crisis Cards

Crisis Cards are the application's standard way of representating a certain crisis. From the outside, they are simple MaterialDesignCard with a picture and a title. However, with the nature of how dynamic our application is, and the way we need to be constantly adding crisis with different types of contents, a simple work around was thought of to create customizable cards. Another way that allowed us to perform this, was the fact that all our crisis were similar.
Normally, a card would be written in XML, however, these cards are written in Java, and then later added to our views. This simplifies the process of having to create endless cards, as well as give our application dynamicity. Each card is responsible to open a Screen, with all the information of that event retrieved from our Database.

### Generation Process

The process of generating a card uses a bottom-up approach:

- Recognize the inner layers and build them
- Embed these inner layers inside outer layers
- Add the outer layers to the main View (MaterialDesignCard)

## Recently Searched Chips

Recently searched tags, or chips, are found in the Search Screen. They are tiny little tags that are clickable and removable. They are the result of the user searching for a criteria. Once the results show up, this little chip is added to the screen. Once a chip is clicked, the onClick listener is called, and the appropriate action is taken. Once the onClose listener is called (chip is being deleted), the chip is selected and removed from the parent view using ViewManager. The Chip is based on the MaterialDesign library, and the styling is done by us. The process of generating the chips is similar to the Crisis Cards, except that we are only passing a chip ID, and the title to be displayed.
