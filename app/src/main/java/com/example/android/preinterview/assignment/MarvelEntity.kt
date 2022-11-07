package com.example.android.preinterview.assignment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarvelEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val thumbnail: String
)