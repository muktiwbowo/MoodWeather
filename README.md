# MoodWeather ☀️😊

A daily mood journal app that automatically logs weather conditions alongside your emotions to help you discover how weather affects your mental state over time.

## What is MoodWeather?

MoodWeather helps you track your emotional wellbeing by connecting your daily moods with weather conditions. Over time, you'll discover patterns like "I feel happier on sunny days" or "storms make me anxious."

## Features

### Current Features
- **Log Your Mood**: Select from 7 different emotions (Happy, Excited, Calm, Sad, Anxious, Angry, Neutral)
- **Track Weather**: Record weather conditions (Sunny, Cloudy, Rainy, Stormy, Snowy, Foggy, Windy)
- **Add Notes**: Write optional journal entries for each mood log
- **View History**: Browse all your past entries organized by date
- **Analytics & Insights**:
  - See mood distribution charts
  - Discover weather-mood correlations
  - Track your logging streak
  - Identify patterns in your emotional wellbeing

### Coming Soon
- Automatic weather detection using live weather API
- Local data persistence with Room database
- Notifications and reminders
- Export data functionality

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3
- **Architecture**: Clean Architecture
  - Domain Layer: Business logic and models
  - Presentation Layer: UI screens and navigation
- **Navigation**: Jetpack Navigation Compose
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 36)

## Project Structure

```
app/src/main/java/com/svault/moodweather/
├── domain/
│   └── model/
│       ├── MoodType.kt           # Enum for mood types
│       ├── WeatherCondition.kt   # Enum for weather conditions
│       └── MoodEntry.kt          # Data model for mood entries
├── presentation/
│   ├── navigation/
│   │   ├── Screen.kt             # Navigation routes
│   │   └── NavGraph.kt           # Navigation graph setup
│   └── screens/
│       ├── home/
│       │   └── HomeScreen.kt     # Dashboard with recent entries
│       ├── addmood/
│       │   └── AddMoodScreen.kt  # Form to log new mood
│       ├── history/
│       │   └── HistoryScreen.kt  # List of all entries
│       └── analytics/
│           └── AnalyticsScreen.kt # Charts and insights
├── ui/theme/
│   ├── Color.kt                  # Custom color palette
│   ├── Theme.kt                  # Material theme setup
│   └── Type.kt                   # Typography
└── MainActivity.kt               # App entry point
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 11 or higher
- Android device or emulator running Android 7.0+

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd MoodWeather
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on your device or emulator

## How to Use

1. **Launch the app** - You'll see the home dashboard with your recent entries
2. **Log a mood** - Tap the "Log Mood" button to add a new entry:
   - Select how you're feeling
   - Choose the current weather
   - Optionally add a note
   - Tap "Save Entry"
3. **View history** - Tap the calendar icon to see all your past entries
4. **Check insights** - Tap the analytics icon to discover patterns

## Color Scheme

The app uses a weather and mood-inspired color palette:
- **Moods**: Happy (Yellow), Excited (Orange), Calm (Blue), Sad (Soft Blue), Anxious (Purple), Angry (Red), Neutral (Gray)
- **Weather**: Sunny (Yellow), Cloudy (Gray), Rainy (Blue), Stormy (Dark), Snowy (Light)

## Screenshots

*(Coming soon)*

## Roadmap

- [ ] Integrate weather API (OpenWeatherMap or WeatherAPI)
- [ ] Implement Room database for local storage
- [ ] Add ViewModel layer with StateFlow
- [ ] Create repository pattern for data management
- [ ] Add dependency injection (Hilt/Koin)
- [ ] Implement data export (CSV/PDF)
- [ ] Add widget support
- [ ] Dark mode enhancements

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License - feel free to use this code for learning purposes.

## Contact

- **Developer**: Mukti
- **Location**: Yogyakarta, Indonesia 🇮🇩
- **Project**: Part of "30 Days, 10 Apps" Android Revival Challenge

---

Made with ❤️ using Kotlin and Jetpack Compose
