package com.svault.moodweather.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddMood : Screen("add_mood")
    data object History : Screen("history")
    data object Analytics : Screen("analytics")
}
