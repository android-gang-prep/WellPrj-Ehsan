package com.ehsannarmani.wellprj_ehsan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ehsannarmani.wellprj_ehsan.database.dao.RockTypeDao
import com.ehsannarmani.wellprj_ehsan.database.dao.WellDao
import com.ehsannarmani.wellprj_ehsan.database.dao.WellLayerDao
import com.ehsannarmani.wellprj_ehsan.database.dao.WellTypeDao
import com.ehsannarmani.wellprj_ehsan.database.entities.RockType
import com.ehsannarmani.wellprj_ehsan.database.entities.Well
import com.ehsannarmani.wellprj_ehsan.database.entities.WellLayer
import com.ehsannarmani.wellprj_ehsan.database.entities.WellType


@Database(
    entities = [
        Well::class,
        WellType::class,
        WellLayer::class,
        RockType::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        lateinit var database: AppDatabase

        fun setup(context: Context){
            database = Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "Session5"
            ).fallbackToDestructiveMigration().build()
        }
    }

    abstract fun wellDao():WellDao
    abstract fun wellLayerDao():WellLayerDao
    abstract fun wellTypeDao():WellTypeDao
    abstract fun rockTypeDao():RockTypeDao
}