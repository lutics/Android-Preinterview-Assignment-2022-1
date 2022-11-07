package com.example.android.preinterview.assignment

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface MarvelDao {

    @Query("SELECT * FROM MarvelEntity ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, MarvelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MarvelEntity)

    @Query("DELETE FROM MarvelEntity WHERE id = :id")
    suspend fun deleteById(id: Int)
}