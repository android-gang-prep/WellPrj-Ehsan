package com.ehsannarmani.wellprj_ehsan.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rock_types")
data class RockType(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String,
    val backgroundColor:String
)