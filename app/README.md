# Desperate Housewives Character Guide

This Android application showcases characters from the TV series "Desperate Housewives". It demonstrates the use of RecyclerView, search functionality, and popup dialogs in a Jetpack Compose and XML hybrid approach.

## Features

1. Display a list of 10+ characters from Desperate Housewives using RecyclerView.
2. Each list item shows the character's image, name, and a brief description.
3. Search functionality to filter characters based on name or description.
4. Clicking on a character opens a popup dialog with more details.
5. A 3-second splash screen before showing the main list.

## Technical Details

- Built with Kotlin and Jetpack Compose
- Uses RecyclerView with a custom adapter for efficient list rendering
- Implements search functionality for filtering the character list
- Uses Coil library for image loading
- Combines Compose UI with traditional View-based layouts

## Project Structure

- `MainActivity.kt`: Main entry point of the application. Contains the Compose UI and RecyclerView integration.
- `item_character.xml`: XML layout for individual character items in the RecyclerView.

## Setup and Running

1. Clone the repository
2. Open the project in Android Studio
3. Run the app on an emulator or physical device

## Requirements

- Android Studio Arctic Fox or later
- Kotlin 1.5.0 or later
- Minimum SDK: 21 (Android 5.0)
- Target SDK: 33 (Android 13)

## Libraries Used

- Jetpack Compose
- AndroidX RecyclerView
- Coil for image loading

## Future Improvements

- Implement data persistence (e.g., Room database)
- Add more detailed character information
- Improve UI/UX design
- Implement unit and UI tests