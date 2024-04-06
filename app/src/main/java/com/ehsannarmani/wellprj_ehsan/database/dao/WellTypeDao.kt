package com.ehsannarmani.wellprj_ehsan.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ehsannarmani.wellprj_ehsan.database.entities.WellType
import kotlinx.coroutines.flow.Flow


@Dao
interface WellTypeDao {
    @Insert
    fun addWellType(vararg wellType: WellType)

    @Query("SELECT * FROM `well_types`")
    fun getWellTypes():Flow<List<WellType>>

    @Query("SELECT * FROM `well_types`")
    fun getLatestWellTypes():List<WellType>
}