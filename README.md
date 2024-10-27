# Angry Birds Project

Welcome to the Angry Birds Project! This project is a fun and interactive game inspired by the classic mobile game, Angry Birds. The objective is to use a slingshot to launch birds at structures to defeat the pigs hiding within.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Gameplay Instructions](#gameplay-instructions)
- [Screens](#screens)
- [Project Structure](#Project-Structure)
- [Repository](#Repository)
- [Contact](#contact)

## **Features**
- **Catapult Mechanics**: Launch various types of birds with different properties.
- **Multiple Screens**: Includes Start, Levels, Game, Winning, and Losing screens.
- **Pause and Popup Menus**: Pause the game with UI popups to restart or change levels.
- **State Management**: Uses `GameStateManager` to switch between screens seamlessly.
- **Physics-based Gameplay**: Birds interact with blocks and pigs with realistic physics.
- **Interactive UI**: Buttons for restarting, switching levels, and navigating menus.

## Technologies Used

- JAVA
- libGDX

## Installation

To get started with the Angry Birds Project, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/Aggarwal2004/AngryBirdsGame-using-libGDX.git

## Usage

Ensure you have Java and Gradle installed:

1. Use the following commands:
   ```bash
   sudo apt install gradle  # Linux  
    brew install gradle      # MacOS
   
-Import the project into your IDE (like IntelliJ or Eclipse) as a Gradle project.


## Gameplay Instructions

- Start the game from the Start Screen using play button.
- Select a level to begin.(initially all levels are laying to the same game play screen)
- Launch birds by pulling the catapult and aiming at the pigs(now just placed only not working).
- Clear all pigs to win the level.
- Pause or Restart the game using the on-screen pause button or using the menu button.
- Win or Lose Screens: Navigate back to levels or restart when a game ends(to initiate this ,when you click on the menu button,that gives you win/lose button click that to go to winning screen and losing screen respectively.

## Screens

- **Start Screen**: Choose levels or resume a saved game.
- **Game Screen**: Core gameplay where players use a catapult to launch birds.
- **Winning Screen**: Displayed when the player wins a level, with options to restart or return to the levels screen.
- **Losing Screen**: Displayed when the player loses a level, with similar options as the winning screen.
- **Popup Menus**: Appears on pause, providing options to resume, restart, or change levels.

## Project Structure

2023152_angry/
- │
- ├── core/                   
- │   ├── src/                
- │       ├── com.mygdx.angry/
- │           ├── AbstractGameScreen.java  
- ├           ├── LevelResultScreen.java
- ├           ├── LevelsScreen.java
- │           ├── GameScreen.java
- │           ├── StartScreen.java
- │           ├── WinningScreen.java
- │           ├── LosingScreen.java
- ├           ├── PauseScreen.java
- ├           ├── SavedGamesScreen.java
- │           ├── GameStateManager.java
- ├           ├── Main.java
- │           ├── Bird.java
- │           ├── Pig.java
- │           ├── Block.java
- │           └── Catapult.java
- ├── gradle/                 
- ├── build.gradle            
- ├── settings.gradle         
- └── README.md  


## Repository
- Below mentioned is the git repository
   ```bash
          https://github.com/Aggarwal2004/AngryBirdsGame-using-libGDX

## Contact

- For any inquiries, feel free to contact the project maintainer:
- Email: ashmit23152@iiitd.ac.in  
- Email: adi23035@iiitd.ac.in
- GitHub: Aggarwal2004(username)

