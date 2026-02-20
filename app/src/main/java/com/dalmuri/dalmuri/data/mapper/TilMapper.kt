package com.dalmuri.dalmuri.data.mapper

import com.dalmuri.dalmuri.data.local.entity.TilEntity
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.Til

fun TilEntity.toDomain(): Til =
    Til(
        id = id,
        title = title,
        learned = learned,
        obstacles = obstacles,
        tomorrow = tomorrow,
        createdAt = createdAt,
        updatedAt = updatedAt,
        emotion = emotion,
        emotionScore = Emotion.fromScore(emotionScore),
        difficultyLevel = difficultyLevel,
        aiComment = aiComment,
    )

fun Til.toEntity(): TilEntity =
    TilEntity(
        id = id,
        title = title,
        learned = learned,
        obstacles = obstacles,
        tomorrow = tomorrow,
        createdAt = createdAt,
        updatedAt = updatedAt,
        emotion = emotion,
        emotionScore = emotionScore?.score,
        difficultyLevel = difficultyLevel,
        aiComment = aiComment,
    )
