package com.ehsannarmani.wellprj_ehsan.ui.screens

import android.app.usage.NetworkStatsManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ehsannarmani.wellprj_ehsan.viewModels.WellViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ehsannarmani.wellprj_ehsan.MainActivity
import com.ehsannarmani.wellprj_ehsan.database.entities.RockType
import com.ehsannarmani.wellprj_ehsan.database.entities.Well
import com.ehsannarmani.wellprj_ehsan.database.entities.WellLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WellScreen(
    wellId:Int?  =null,
    navController: NavHostController,
    viewModel: WellViewModel = viewModel()
) {

    val context = LocalContext.current
    LaunchedEffect(Unit){
        if (wellId == null) return@LaunchedEffect
        viewModel.getWell(wellId)
    }

    val networkAvailable by MainActivity.networkAvailable.collectAsState()

    val rockTypes by viewModel.rockTypes.collectAsState()
    val wellLayers by viewModel.wellLayers.collectAsState()
    val well by viewModel.well.collectAsState()

    val fieldsStyle = TextStyle(fontSize = 13.sp)
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Well Information") }, actions = {
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = "Back")
            }
        })
    }) {
        if (rockTypes.isEmpty() || wellLayers.isEmpty()) return@Scaffold
        val wellName = remember {
            mutableStateOf(well?.wellName.orEmpty())
        }
        val gasOilDepth = remember {
            mutableStateOf(well?.gasOilDepth?.toString() ?: "")
        }
        val capacity = remember {
            mutableStateOf(well?.capacity?.toString() ?: "")
        }
        val layers = remember {
            mutableStateListOf(*(if (well == null) emptyList() else wellLayers.filter { it.wellId == well!!.id }).toTypedArray())
        }

        val newDepthFrom = remember {
            mutableStateOf("0")
        }
        val newDepthTo = remember {
            mutableStateOf("")
        }

        val rockTypeMenuOpen = remember {
            mutableStateOf(false)
        }
        val selectedRockType = remember {
            mutableStateOf<RockType?>(null)
        }

        LaunchedEffect(layers.count()){
            newDepthFrom.value = layers.lastOrNull()?.endPoint?.toString() ?: "0"
        }


        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(it)
        ) {
            OutlinedTextField(
                value = wellName.value,
                onValueChange = { wellName.value = it },
                placeholder = {
                    Text(text = "Well Name")
                })
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                OutlinedTextField(
                    value = gasOilDepth.value,
                    onValueChange = { gasOilDepth.value = it },
                    modifier = Modifier.weight(.7f),
                    placeholder = {
                        Text(text = "Depth of Gas or Oil Extraction",style = fieldsStyle)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = fieldsStyle
                )
                OutlinedTextField(
                    value = capacity.value,
                    onValueChange = { capacity.value = it },
                    modifier = Modifier.weight(.3f),
                    placeholder = {
                        Text(text = "Well Capacity",style = fieldsStyle)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = fieldsStyle
                )
            }
            Text(text = "Rock Layers", modifier = Modifier.padding(vertical = 22.dp))
            Box {
                Button(onClick = { rockTypeMenuOpen.value = true }, shape = RoundedCornerShape(8.dp)) {
                    if (selectedRockType.value == null) {
                        Text(text = "Pick Rock Layer")
                    } else {
                        Text(text = selectedRockType.value!!.name)
                    }
                }
                DropdownMenu(
                    expanded = rockTypeMenuOpen.value,
                    onDismissRequest = {
                        rockTypeMenuOpen.value = false
                    }
                ) {
                    rockTypes.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.name)
                            },
                            onClick = {
                                selectedRockType.value = it
                                rockTypeMenuOpen.value = false
                            }
                        )
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                OutlinedTextField(value = newDepthFrom.value, onValueChange = {
                    newDepthFrom.value = it
                }, modifier = Modifier.weight(.35f), placeholder = {
                    Text(text = "From Depth", style = fieldsStyle)
                },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = fieldsStyle,
                    readOnly = true
                )
                OutlinedTextField(value = newDepthTo.value, onValueChange = {
                    newDepthTo.value = it
                }, modifier = Modifier.weight(.35f), placeholder = {
                    Text(text = "To Depth",
                        style = fieldsStyle)
                },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = fieldsStyle
                )

                TextButton(onClick = {
                    if (selectedRockType.value != null && newDepthFrom.value.isNotEmpty() && newDepthTo.value.isNotEmpty()) {
                        println("layers: ${layers.toList()}")
                        println("selected: ${selectedRockType.value!!.id}")
                        if (layers.none { it.rockTypeId == selectedRockType.value!!.id }){
                            layers.add(
                                WellLayer(
                                    wellId = 0,
                                    rockTypeId = selectedRockType.value!!.id,
                                    startPoint = newDepthFrom.value.toInt(),
                                    endPoint = newDepthTo.value.toInt()
                                )
                            )
                            newDepthTo.value = ""
                        }else{
                            Toast.makeText(
                                context,
                                "You cant add more than one ${selectedRockType.value!!.name} rock",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        
                    }
                }, modifier = Modifier.weight(.3f)) {
                    Text(text = "Add layer")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(layers) {layer->
                    LayerItem(layer = layer, onDelete = {
                        layers.remove(layer)
                    }, rockTypes = rockTypes)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = {
                    if (wellName.value.isNotEmpty() && gasOilDepth.value.isNotEmpty() && capacity.value.isNotEmpty() && layers.isNotEmpty()){
                        if (layers.maxOf { it.endPoint } <= gasOilDepth.value.toInt()){
                            if (well == null){
                                viewModel.addWell(
                                    well = Well(
                                        wellTypeId = 1,
                                        wellName = wellName.value,
                                        gasOilDepth = gasOilDepth.value.toInt(),
                                        capacity = capacity.value.toInt()
                                    ),
                                    layers = layers
                                )
                            }else{
                                viewModel.updateWell(
                                    well = Well(
                                        id = wellId!!,
                                        wellTypeId = 1,
                                        wellName = wellName.value,
                                        gasOilDepth = gasOilDepth.value.toInt(),
                                        capacity = capacity.value.toInt()
                                    ),
                                    layers = layers
                                )
                            }
                            navController.popBackStack()
                        }else{
                            Toast.makeText(context, "Max layer end point should less than or equal to gas oil", Toast.LENGTH_SHORT).show()
                        }

                    }
                }, shape = RoundedCornerShape(8.dp), enabled = networkAvailable) {
                    Text(text = "Submit")
                }
            }
        }
    }
}

@Composable
fun LayerItem(
    layer: WellLayer,
    rockTypes: List<RockType>,
    onDelete: () -> Unit
) {
    val rockType = rockTypes.find { it.id == layer.rockTypeId }
    Row(modifier= Modifier
        .fillMaxWidth()
        .padding(12.dp)) {
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
        Column(modifier=Modifier.fillMaxWidth()) {
            Text(text = rockType?.name.orEmpty())
            Text(text = "From: ${layer.startPoint} to ${layer.endPoint}")
        }
    }
}