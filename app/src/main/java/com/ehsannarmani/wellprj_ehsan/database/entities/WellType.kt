package com.ehsannarmani.wellprj_ehsan.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("well_types")
data class WellType(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String
)
