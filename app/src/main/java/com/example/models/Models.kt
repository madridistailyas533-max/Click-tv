package com.example.models

import java.io.Serializable

enum class ItemType {
    MOVIE, SERIES, LIVE
}

data class Server(
    val name: String,
    val url: String
) : Serializable

data class VodItem(
    val id: String,
    val title: String,
    val description: String,
    val type: ItemType,
    val poster: Int,
    val backdrop: Int,
    val year: String,
    val category: String,
    val rating: String,
    val servers: List<Server>
) : Serializable

data class LiveCategory(
    val id: String,
    val title: String,
    val desc: String,
    val icon: Int, // Drawable res
    val color: Long,
    val borderColor: Long
) : Serializable

data class LiveChannel(
    val id: String,
    val catId: String,
    val title: String,
    val servers: List<Server>
) : Serializable
