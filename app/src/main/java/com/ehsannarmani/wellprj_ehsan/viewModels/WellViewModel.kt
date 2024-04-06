package com.ehsannarmani.wellprj_ehsan.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehsannarmani.wellprj_ehsan.database.AppDatabase
import com.ehsannarmani.wellprj_ehsan.database.entities.RockType
import com.ehsannarmani.wellprj_ehsan.database.entities.Well
import com.ehsannarmani.wellprj_ehsan.database.entities.WellLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WellViewModel: ViewModel() {

    private val _wells = MutableStateFlow(emptyList<Well>())
    val wells = _wells.asStateFlow()

    private val _well = MutableStateFlow<Well?>(null)
    val well = _well.asStateFlow()

    private val _wellLayers = MutableStateFlow(emptyList<WellLayer>())
    val wellLayers = _wellLayers.asStateFlow()

    private val _rockTypes = MutableStateFlow(emptyList<RockType>())
    val rockTypes = _rockTypes.asStateFlow()

    val database = AppDatabase.database

    val wellDao = database.wellDao()
    val wellLayerDao = database.wellLayerDao()
    val rockTypeDao = database.rockTypeDao()

    init {
        getWells()
        getWellLayers()
        getRockTypes()
    }

    fun getWells(){
        viewModelScope.launch (Dispatchers.IO){
           wellDao.getWells().collect{wells->
               _wells.update { wells }
           }
        }
    }

    fun getWell(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            wellDao.getWell(id).collect{well->
                _well.update { well }
            }
        }
    }

    fun getWellLayers(){
        viewModelScope.launch (Dispatchers.IO){
           wellLayerDao.getWellLayers().collect{layers->
               _wellLayers.update { layers }
           }
        }
    }

    fun getRockTypes(){
        viewModelScope.launch (Dispatchers.IO){
           rockTypeDao.getRockTypes().collect{rockTypes->
               _rockTypes.update { rockTypes }
           }
        }
    }

    fun addWell(
        well: Well,
        layers:List<WellLayer>
    ){
        viewModelScope.launch(Dispatchers.IO) {
            wellDao.addWell(well).also {wellId->
                wellLayerDao.addWellLayer(*layers.map { it.copy(wellId = wellId.toInt()) }.toTypedArray())
            }
        }
    }
    fun updateWell(
        well: Well,
        layers: List<WellLayer>
    ){
        viewModelScope.launch(Dispatchers.IO){
            wellLayerDao.deleteWellLayers(well.id)
            wellLayerDao.addWellLayer(*layers.map { it.copy(wellId = well.id) }.toTypedArray())
            wellDao.updateWell(well)
        }
    }

}