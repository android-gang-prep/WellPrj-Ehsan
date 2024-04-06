package com.ehsannarmani.wellprj_ehsan.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("well_layers")
data class WellLayer(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val wellId:Int,
    val rockTypeId:Int,
    val startPoint:Int,
    val endPoint:Int
)
