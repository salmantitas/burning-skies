![Burning Skies Banner](https://user-images.githubusercontent.com/26829338/225977134-5e694a01-c665-44cc-a73a-80b780bd8ea1.png)

Overview
 -
 - A retro-style top-down scrolling shooter game, inspired by the classics (Raptor and Demonstar)
 - Procedurally generated waves of enemies
 
 Play The Game Here!
 -
 - https://monkeys-in-distress.itch.io/burning-skies

![Burning Skies Heading HOW TO PLAY](https://user-images.githubusercontent.com/26829338/226021406-60cb286e-480e-4652-a5fa-1603657fe2b4.png)

Player
-
- Control an airplane that can move in four directions.
- Shoot enemies to destroy them and earn score.
- Avoid damage by dodging enemy bullets and collisions. 

Controls
-
- The WASD or directional keys can be used to control the player’s movements. 
- The SPACEBAR is used to shoot.

UI
-

Menu
-
- The game features a very simplistic menu which allows the player to navigate.
- Play and Quit buttons do as their names suggest.
- Help button explains the control scheme.
- A Transition Menu opens after clicking the Play button. This does not offer any extra features as of yet.
- On player death, the Game Over Menu is presented. The player can quit or return to the Main Menu.
- The menus are navigable using mouse of keyboard controls

HUD
- 
- The HUD only shows up during Play, Pause or Transition Menus. It shows the player’s health, level and score.

![Burning Skies Heading TECHNICAL](https://user-images.githubusercontent.com/26829338/226022076-daf81f99-85b8-46a8-bd98-e47e5ca419f9.png)

Physics
-
- The player’s movements are affected by accelaration and friction.

Game Camera
- 
- The game features a fixed, orthogonal camera which is centered at the player’s origin point. The camera does not follow the player.

Graphics
- 
- The game reads textures from a resource directory and formats them into spritesheets. These sprites are separated in-game and rendered on-screen.
- The game can also render collision bounds as a debug feature.
- A scrolling background provides the illusion of flying over a sea. A spritesheet with different water textures are used to change the image to implement animation.
- When enemies die, an explosion animation plays before both disappear.

Sounds
-
- The game reads audio files from a resource directory and formats them into clips. These sounds can be played during the game.
- The background music is played throughout the entire game.
- A bullet sound effect is used when bullets are fired.
- An explosion sound effect is used when enemies die.

Procedural Generation
-
- Enemies are generated in varying numbers, in predetermined patterns and locations

Enemies
- 
- The game only has one type of enemy as of now. Enemy health is randomly generated from a pre-determined range.

Drones
- Health: 2 - 4
- Flies down the screen and shoots a bullet at a given interval.
