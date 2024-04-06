package com.ehsannarmani.wellprj_ehsan.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ehsannarmani.wellprj_ehsan.database.entities.Well
import kotlinx.coroutines.flow.Flow


@Dao
interface WellDao {
    @Insert
    fun addWell(well: Well):Long

    @Insert
    fun addWell(vararg well: Well)

    @Query("SELECT * FROM `wells`")
    fun getWells():Flow<List<Well>>

    @Query("SELECT * FROM `wells` WHERE id = :id")
    fun getWell(id:Int):Flow<Well>

    @Query("SELECT * FROM `wells`")
    fun getLatestWells():List<Well>

    @Update
    fun updateWell(well: Well)
}