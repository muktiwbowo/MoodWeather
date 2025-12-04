package com.svault.moodweather.domain.model

import androidx.compose.ui.graphics.Color
import com.svault.moodweather.ui.theme.*

enum class MoodType(
    val displayName: String,
    val emoji: String,
    val color: Color
) {
    HAPPY("Happy", "😊", HappyYellow),
    EXCITED("Excited", "🤩", ExcitedOrange),
    CALM("Calm", "😌", CalmBlue),
    SAD("Sad", "😢", SadBlue),
    ANXIOUS("Anxious", "😰", AnxiousPurple),
    ANGRY("Angry", "😠", AngryRed),
    NEUTRAL("Neutral", "😐", NeutralGray)
}
