package com.example.android.preinterview.assignment

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.sql.Timestamp
import java.time.Instant

class MarvelPagingSource(
    private val client: HttpClient
) : PagingSource<Int, MarvelVO.Response.Characters>() {

    companion object {
        const val apiKey = "4f033ec0680a6db87a73e871f1ffdf0e"
        const val privateKey = "e0816eaec7ad530389bfd95897841c9f9b189427"
    }

    override fun getRefreshKey(
        state: PagingState<Int, MarvelVO.Response.Characters>
    ): Int? = null

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MarvelVO.Response.Characters> {
        val offset = params.key ?: 0

        return try {
            val limit = params.loadSize

            val response = getData(
                offset,
                limit
            )

            val data: MarvelVO.Response.Data<List<MarvelVO.Response.Characters>> = response.data

            val prevKey = if (offset == 0 || offset == data.offset || offset - params.loadSize >= data.total) null else offset

            val nextKey = if (offset + params.loadSize <= data.total) {
                offset + params.loadSize
            } else {
                null
            }

            LoadResult.Page(
                data = data.results,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    suspend fun getData(
        position: Int,
        size: Int
    ): MarvelVO.Response<List<MarvelVO.Response.Characters>> {
        val response = client.get("/v1/public/characters") {
            val time = Timestamp.from(Instant.now()).time

            parameter("offset", position)
            parameter("limit", size)

            parameter("apikey", apiKey)
            parameter("hash", md5("$time$privateKey$apiKey"))
            parameter("ts", time)
        }

        return response.body()
    }
}