package com.ehsannarmani.wellprj_ehsan.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ehsannarmani.wellprj_ehsan.database.entities.RockType
import kotlinx.coroutines.flow.Flow


@Dao
interface RockTypeDao {
    @Insert
    fun addRockType(vararg rockType: RockType)

    @Query("SELECT * FROM `rock_types`")
    fun getRockTypes():Flow<List<RockType>>

    @Query("SELECT * FROM `rock_types`")
    fun getLatestRockTypes():List<RockType>
}