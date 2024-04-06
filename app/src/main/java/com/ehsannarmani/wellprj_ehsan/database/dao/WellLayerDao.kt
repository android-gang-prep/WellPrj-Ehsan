package com.ehsannarmani.wellprj_ehsan.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ehsannarmani.wellprj_ehsan.database.entities.WellLayer
import kotlinx.coroutines.flow.Flow


@Dao
interface WellLayerDao {
    @Insert
    fun addWellLayer(vararg wellLayer: WellLayer)

    @Query("SELECT * FROM `well_layers`")
    fun getWellLayers():Flow<List<WellLayer>>

    @Query("SELECT * FROM `well_layers`")
    fun getLatestWellLayers():List<WellLayer>

    @Query("DELETE FROM `well_layers` WHERE `wellId`=:wellId")
    fun deleteWellLayers(wellId:Int)
}