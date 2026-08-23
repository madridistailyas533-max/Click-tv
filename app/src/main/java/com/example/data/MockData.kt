package com.example.data

import com.example.R
import com.example.models.ItemType
import com.example.models.LiveCategory
import com.example.models.LiveChannel
import com.example.models.Server
import com.example.models.VodItem

object MockData {
    val vodContent = listOf(
        VodItem(
            id = "movie1",
            title = "The Last Stand",
            description = "An epic action movie about the last stand of humanity against an alien invasion.",
            type = ItemType.MOVIE,
            poster = R.drawable.img_poster_1_1787320957419,
            backdrop = R.drawable.img_hero_1787320940018,
            year = "2024",
            category = "Action",
            rating = "4.8",
            servers = listOf(
                Server("Server 1", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
            )
        ),
        VodItem(
            id = "series1",
            title = "Mystic Forest",
            description = "A fantasy series following a group of adventurers through a magical forest.",
            type = ItemType.SERIES,
            poster = R.drawable.img_poster_2_1787320974582,
            backdrop = R.drawable.img_hero_1787320940018,
            year = "2023",
            category = "Fantasy",
            rating = "4.5",
            servers = listOf(
                Server("Server 1", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
            )
        )
    )

    val liveCategories = listOf(
        LiveCategory(
            id = "alwan",
            title = "باقة الوان الرياضية",
            desc = "أقوى القنوات والبطولات الرياضية",
            icon = R.drawable.ic_launcher_foreground, // Placeholder
            color = 0xFFE50914,
            borderColor = 0xFFE50914
        ),
        LiveCategory(
            id = "bein",
            title = "باقة بي ان سبورت",
            desc = "جميع قنوات beIN Sports المشفرة",
            icon = R.drawable.ic_launcher_foreground, // Placeholder
            color = 0xFF9333EA,
            borderColor = 0xFF9333EA
        )
    )

    val liveChannels = listOf(
        LiveChannel(
            id = "ch1",
            catId = "alwan",
            title = "Alwan Sport 1",
            servers = listOf(Server("Auto", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"))
        ),
        LiveChannel(
            id = "ch2",
            catId = "bein",
            title = "beIN Premium 1",
            servers = listOf(Server("Auto", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"))
        )
    )
}
