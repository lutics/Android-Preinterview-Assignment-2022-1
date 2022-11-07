package com.example.android.preinterview.assignment

import androidx.annotation.Keep

@Keep
class MarvelVO {

    @Keep
    data class Response<T>(
        val code: Int,
        val status: String,
        val data: Data<T>
    ) {

        @Keep
        data class Data<T>(
            val offset: Int,
            val limit: Int,
            val total: Int,
            val count: Int,
            val results: T
        )

        @Keep
        data class Characters(
            val id: Int,
            val name: String,
            val description: String,
            val thumbnail: Thumbnail,
            val resourceURI: String,
            val comics: Comics,
            val series: Series,
            val stories: Stories,
            val events: Events,
            val urls: List<URL>
        ) {

            @Keep
            data class Thumbnail(
                val path: String,
                val extension: String
            )

            @Keep
            data class Item(
                val name: String,
                val resourceURI: String,
                val type: String?
            )

            @Keep
            data class Comics(
                val available: Int,
                val collectionURI: String,
                val items: List<Item>,
                val returned: Int
            )

            @Keep
            data class Stories(
                val available: Int,
                val collectionURI: String,
                val items: List<Item>,
                val returned: Int
            )

            @Keep
            data class Series(
                val available: Int,
                val collectionURI: String,
                val items: List<Item>,
                val returned: Int
            )

            @Keep
            data class Events(
                val available: Int,
                val collectionURI: String,
                val items: List<Item>,
                val returned: Int
            )

            @Keep
            data class URL(
                val type: String,
                val url: String
            )
        }
    }
}