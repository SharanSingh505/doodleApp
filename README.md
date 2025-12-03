**Doodle App – IA08 Technical I**

Course: CISC682 – Introduction to Human Computer Interaction
Date: Fall 2025
Language: Kotlin
Framework: Jetpack Compose

Overview

- This application is a simple doodle/drawing tool built for Android phones and tablets. It is implemented entirely using Jetpack Compose, without any XML layouts or traditional custom View-based onDraw() methods.

The goal of this assignment prototype is to demonstrate an understanding of:

- Declarative UI design
- State management with remember, mutableStateOf, and mutableStateListOf
- Gesture handling using Compose’s pointerInput and detectDragGestures
- Drawing graphics using the Compose Canvas API
- Managing multi-stroke drawing and UI tool panels

The app provides an intuitive drawing experience with adjustable brush controls and real-time canvas rendering.


Features
Core Requirements
 Drawing Canvas
  - Uses Canvas and detectDragGestures to capture touch input.
  - User strokes are drawn in real time using Path.
  - Each stroke is stored as a DrawingStroke data class containing:
    - Path
    - Brush color
    - Brush size
    - Brush pattern (Solid or Dotted)

 Tool Panel

 A control interface allowing users to customize their brush:

  - Brush Size Slider
  - Adjustable from 5px to 50px.
  - Brush Colors
  - A palette of 10 preset colors.

    
Brush Pattern Picker

Users can choose between:
  - Solid
  - Dotted (implemented with PathEffect.dashPathEffect)

Bonus Features 
- Undo Functionality
- Users can undo their most recent stroke.


How to Run
1. Clone the Repository
https://github.com/SharanSingh505/doodleApp
2. Open in Android Studio
Open Android Studio → Open Project
Select the cloned project folder.
3. Sync Gradle
Android Studio will automatically download dependencies and configure Compose.
4. Run the App
Connect an Android device or run an AVD Emulator (Pixel 6 recommended).
Click the Run button (Shift + F10)

Developed by Sharanjit Singh

