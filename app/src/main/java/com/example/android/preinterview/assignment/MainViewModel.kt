package com.example.android.preinterview.assignment

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val client: HttpClient,
    database: Database
) : ViewModel() {

    private val dao = database.marvelDao()

    val listPager = Pager(
        PagingConfig(20)
    ) {
        MarvelPagingSource(
            client
        )
    }.flow

    val cartPager = Pager(
        PagingConfig(20)
    ) {
        dao.pagingSource()
    }.flow

    suspend fun addCart(
        id: Int,
        name: String,
        thumbnail: String
    ) {
        val entity = MarvelEntity(
            id = id,
            name = name,
            thumbnail = thumbnail
        )

        dao.insert(entity)
    }

    suspend fun removeCart(
        id: Int
    ) {
        dao.deleteById(id)
    }
}