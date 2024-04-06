package com.ehsannarmani.wellprj_ehsan.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ehsannarmani.wellprj_ehsan.viewModels.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ehsannarmani.wellprj_ehsan.MainActivity
import com.ehsannarmani.wellprj_ehsan.navigation.Routes
import com.ehsannarmani.wellprj_ehsan.ui.theme.font
import com.ehsannarmani.wellprj_ehsan.utils.toColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {

    val context = LocalContext.current

    val wells by viewModel.wells.collectAsState()
    val wellLayers by viewModel.wellLayers.collectAsState()
    val rockTypes by viewModel.rockTypes.collectAsState()

    val networkAvailable by MainActivity.networkAvailable.collectAsState()


    if (wells.isNotEmpty() && wellLayers.isNotEmpty() && rockTypes.isNotEmpty()) {
        val selectedWell = remember {
            mutableStateOf(wells.first())
        }
        val wellNameDropDownOpen = remember {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Well Name:")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Button(
                            onClick = { wellNameDropDownOpen.value = true },
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = selectedWell.value.wellName)
                        }
                        DropdownMenu(
                            expanded = wellNameDropDownOpen.value,
                            onDismissRequest = {
                                wellNameDropDownOpen.value = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            wells.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = it.wellName)
                                    },
                                    onClick = {
                                        selectedWell.value = it
                                        wellNameDropDownOpen.value = false
                                    }
                                )
                            }
                        }
                    }
                    Button(onClick = {
                        navController.navigate(Routes.Well.route + "/${selectedWell.value.id}")
                    }, shape = RoundedCornerShape(0.dp)) {
                        Text(text = "Edit")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(bottom = 16.dp)
                ) {
                    val maxDepth = selectedWell.value.gasOilDepth
                    val height = maxHeight.value

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        val layers = wellLayers
                            .filter { it.wellId == selectedWell.value.id }

                        layers
                            .forEach { wellLayer ->
                                val rockType = rockTypes.find { it.id == wellLayer.rockTypeId }
                                val depth = wellLayer.endPoint - wellLayer.startPoint
                                val boxHeight = ((height * depth) / maxDepth).dp
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(boxHeight)
                                            .background(
                                                (rockType?.backgroundColor ?: "#313131").toColor()
                                            ), contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = rockType?.name.orEmpty())
                                    }
                                }
                            }
                        val depth = selectedWell.value.gasOilDepth - layers.last().endPoint
                        val gasOilHeight = ((height * depth) / maxDepth).dp
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(gasOilHeight)
                                .background(Color.Black), contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Oil / Gas", color = Color.White)
                        }
                    }
                }
                Text(text = "Well Capacity: ${selectedWell.value.capacity} m3")
            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = {
                   if (networkAvailable){
                       navController.navigate(Routes.Well.route)
                   }else{
                       Toast.makeText(context, "Make sure you are connected to internet", Toast.LENGTH_SHORT).show()
                   }
                },
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

    }

}