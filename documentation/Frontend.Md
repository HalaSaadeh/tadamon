# Front-End Development of the Application, UI, and UX

## Design

### Name and Logo

The word "Tadamon" means solidarity in arabic. The idea of Tadamon comes from the fact in Lebanon, there is a strong need for an NGO that "centralizes" all crises happening, and redirects help within one application. Tadamon could become the standard of the crisis happening around Lebanon: Tadamon can become the first application you open in case you want to donate or grant help to anyone affected by any type of crisis.

![Logo Light](./media/logo_light.png)

![Logo Light](./media/logo_dark.png)

### Plans

The nature of the application, being a cruicial application for everyone in need of help, drove us to create a layout that was easy to navigate, and very straightforward. The screens are structured in a way that there wouldn't be any ambiguous travelling back and forth, nor any confusion as to where something is.

### Process

The designs rely on very simplistic modern layouts. Inspired by most applications these days, Tadamon features Bottom Tab Navigation, that can seamlessly switch between three main screens: _Discover_, _Search_, and _Profile_.  
For **Dicover**, which is supposed to be the "Home" Screen of our app, the user will be able to look at his previously participated-in crises, whether they were a donator or a volunteer, as well as be able to discover others. Based on the user's location, they will receive different suggestions as to where to go next, or where donation is needed.  
For **Search**, the user is able search for a specific on-going, or closed crisis. The user can also look the previously searched crisis, for accessibility and useability.  
For **Profile**, the user will be able to look his own data, such as profile picture, biography and how many times they volunteered/donated. Moreover, they will be able to track which crisis they are or were involved in.

### Pre-Production

To have our application well organized and structured, the UI designs were created on a templating app, Adobe XD.

- Assets are Scalable Vector Graphics, or SVG. All the SVG's used in the app were either designed in Adobe XD and exported, or gotten from Flaticon.
- The use of SVG means that these icons, can be scaled up and down without any loss of resolution, and can be employed on any screen of any density.
- Adobe XD Templates will be included in the project structure.

![Account Creation Screen Step 4](./media/AccountCreationScreen4.jpg)

![Donation Screen](./media/DonationScreen.jpg)

![Profile Screen](./media/ProfileScreen.jpg)

## Colors, Dimensions & Layouts

### Color Palette

The application incorporates a modern UI design, with vibrant contrast colors. The palette used is a warm-fall color selection. All colors are saved in the colors.xml file

![Color Palette](./media/ColorPalette.png)

### Dimensions & Screen Layouts

The application uses a homogeneous dimension setting. All dimensions are saved in the dimens.xml file.
All screens have a uniform distribution layout (See Screenshots at the end).  
Activities use a Linear Layout, embedded in a Constraint Layout. All the screens follow this same application. The use of a Linear Layout allows us to place everything correctly, without any deformation with smaller or bigger screen sizes.  
When Linear Layouts fail, we require the help of an external library developed by Google called FlexLayout. The FlexLayout uses css flex, and offers more versatility.
Screen layouts include a 15dp margin all around. Separation between view is diverse but ranges from 10 to 20dp (unless extreme cases).  
Buttons and EditTexts (TextView) are designed to have a rounded edge of 8dp. All buttons support custom backgrounds created in android studio using XML Shape.  
Font sizes are properly divided into categories: very small, small, medium, large and very large. Depending on the design, each font is assigned.
We are using DP and SP, which are measurement units that are compatible with all screen sizes, depending on the screen pixel density. In case any View would be implemented programmatically, a conversion function from pixel to dp/sp would be employed.

## Navigation

### Screen Navigations

All screen use regular navigations with intents. No fragments were implemented because each screen is an entity on its own.

### Bottom Tab Navigation

Our application features a bottom tab navigation, meaning, the user can navigate between three main screens with a press of an icon. The three main screens are Discover, Search and Profile.  
Bottom Tab Navigation is a library implemented using Material Design.

## UX and Features

### Cards

Crisis are displayed as clickable cards. Each Card preview has the Crisis Title and a photo description of the Crisis. On click, the Crisis Activity opens, displaying all the relevant information regarding the crisis.  
The Crisis details include the title, the photo, how many days left to participate, and a descritption. Moreover, We can find the total donations and amounts needed, as well as how many people donated. Next, we see a list of randomly picked donators and volunteers.  
Each crisis incorporates a volunteering and donating button. Each takes the user to the appropriate layout.

### Donation & Volunteering

Donation Screen contains a keypad, with money shortcuts for easy donations. All the keypad buttons are handcrafted, and the functionality is all designed in the backend. Once a user chooses an amount and confirms, they are taken to a Thank You page.  
Following the same concept, the volunteering screen incorporates all the relevant information of the event, added to it some information on how to volunteer, who to contact, and a Google Map for where to go. Finally, the volunteer button will take the user to a Thank you page.

### Thank You Page

A simple screen to thank the user for their donation or volunteering. After a certain elapsed time, the page will disappear

### Elevated Layouts

All cards implemented have a certain elevated layout to create the illusion of a card.

### Multi-Scrolling

In all main screens, the user can horizontally scroll all the cards, depending on the category. Moreover, in the Search screen, the user can navigate crisis categories to browse that criteria.  
Multiscrolling is enabled, meaning the user can both horizontally scroll a certain part of a page, while vertically scrolling the rest of that page.

### Quick Search

Quick search is implemented in our design, as it is an important feature any application should have. The user will be able browse catgeories easily with a simple button click. The catgeories proposed for crisis are Natural, Technological, Financial, Social, Winter, Wildfires and Health. In the future, more categories can be added, depending on the crisis that arises.

### Search History

The application features a search history, where all previously searched for keywords and crisis will be saved as little chips. Clicking on that chip will enable us to open the search results of that criteria. These little chips are also clearable, meaning the user can add and remove as much as they want.

### Webview

In the profile screen, and when clicking on the settings button, we have the option to either log out of the application, or fill a feedback form that gets sent to us. The feedback form opens in a WebView.

## Animations and Transitions

### Animations

Animations were kept minimal, and they were added at the home screen. The rectangle under the logo of tadamon gets translated and disappears, then the application opens.  
In the Thank You screen, the title and body are dragged down and appear on sight.

### Transitions

All screens incorporate sliding behavior between them.
