package com.ehsannarmani.wellprj_ehsan.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("wells")
data class Well(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val wellTypeId:Int,
    val wellName:String,
    val gasOilDepth:Int,
    val capacity:Int
)
