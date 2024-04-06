package com.ehsannarmani.wellprj_ehsan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ehsannarmani.wellprj_ehsan.database.AppDatabase
import com.ehsannarmani.wellprj_ehsan.database.entities.RockType
import com.ehsannarmani.wellprj_ehsan.database.entities.Well
import com.ehsannarmani.wellprj_ehsan.database.entities.WellLayer
import com.ehsannarmani.wellprj_ehsan.database.entities.WellType
import com.ehsannarmani.wellprj_ehsan.navigation.Routes
import com.ehsannarmani.wellprj_ehsan.ui.screens.HomeScreen
import com.ehsannarmani.wellprj_ehsan.ui.screens.WellScreen
import com.ehsannarmani.wellprj_ehsan.ui.theme.WellPrjEhsanTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class MainActivity : ComponentActivity() {

    companion object{
        val _networkAvailable = MutableStateFlow(true)
        val networkAvailable = _networkAvailable.asStateFlow()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppDatabase.setup(this)
        insertInitialData()

//        lifecycleScope.launch(Dispatchers.IO){
//            while (true){
//                delay(500)
//                checkInternet(
//                    onFail =  {
//                        _networkAvailable.update { false }
//                    },
//                    onOk = {
//                        _networkAvailable.update { true }
//                    }
//                )
//            }
//        }
        setContent {
            WellPrjEhsanTheme {

                val context = LocalContext.current


                val networkAvailable by MainActivity.networkAvailable.collectAsState()

                LaunchedEffect(networkAvailable){
                    if (!networkAvailable){
                        Toast.makeText(context, "You are diconnected", Toast.LENGTH_SHORT).show()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.Home.route) {
                        composable(Routes.Home.route) {
                            HomeScreen(navController = navController)
                        }
                        composable(Routes.Well.route+"/{well_id}", arguments = listOf(
                            navArgument("well_id"){type = NavType.IntType}
                        )) {
                            WellScreen(
                                wellId = it.arguments?.getInt("well_id"),
                                navController = navController
                            )
                        }
                        composable(Routes.Well.route) {
                            WellScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }


    fun insertInitialData() {
        val database = AppDatabase.database
        val rockTypeDao = database.rockTypeDao()
        val wellDao = database.wellDao()
        val wellTypeDao = database.wellTypeDao()
        val wellLayerDao = database.wellLayerDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val rockTypes = rockTypeDao.getLatestRockTypes()
            val wells = wellDao.getLatestWells()
            val wellLayers = wellLayerDao.getLatestWellLayers()
            val wellTypes = wellTypeDao.getLatestWellTypes()

            if (rockTypes.isEmpty()) {
                rockTypeDao.addRockType(
                    RockType(name = "Argillite", backgroundColor = "#e52B50"),
                    RockType(name = "Breccia", backgroundColor = "#ffbf00"),
                    RockType(name = "Chalk", backgroundColor = "#9966cc"),
                    RockType(name = "Chert", backgroundColor = "#fbceb1"),
                    RockType(name = "Coal", backgroundColor = "#7fffd4"),
                    RockType(name = "Conglomerate", backgroundColor = "#007fff"),
                    RockType(name = "Dolomite", backgroundColor = "#0095b6"),
                    RockType(name = "Limestone", backgroundColor = "#800020"),
                    RockType(name = "Marl", backgroundColor = "#de3163"),
                    RockType(name = "Mudstone", backgroundColor = "#f7e7ce"),
                    RockType(name = "Sandstone", backgroundColor = "#7fff00"),
                    RockType(name = "Shale", backgroundColor = "#c8a2c8"),
                    RockType(name = "Tufa", backgroundColor = "#bfff00"),
                    RockType(name = "Wackestone", backgroundColor = "#ffff00"),
                )
            }
            if (wellTypes.isEmpty()){
                wellTypeDao.addWellType(
                    WellType(name = "Well"),WellType(name = "Section")
                )
            }
            if (wells.isEmpty()){
                wellDao.addWell(
                    Well(
                        wellTypeId = 1,
                        wellName = "Yolka #12",
                        gasOilDepth = 4500,
                        capacity = 980000000
                    ),
                    Well(
                        wellTypeId = 1,
                        wellName = "Kazan #12",
                        gasOilDepth = 4230,
                        capacity = 1080000000
                    ),
                    Well(
                        wellTypeId = 1,
                        wellName = "Kazan #13",
                        gasOilDepth = 4830,
                        capacity = 780000000
                    ),
                )
            }
            if (wellLayers.isEmpty()) {
                wellLayerDao.addWellLayer(
                    WellLayer(wellId = 1, rockTypeId = 10, startPoint = 0, endPoint = 800),
                    WellLayer(wellId = 1, rockTypeId = 3, startPoint = 800, endPoint = 1430),
                    WellLayer(wellId = 1, rockTypeId = 2, startPoint = 1430, endPoint = 1982),
                    WellLayer(wellId = 1, rockTypeId = 11, startPoint = 1982, endPoint = 2648),
                    WellLayer(wellId = 1, rockTypeId = 6, startPoint = 2648, endPoint = 3312),
                    WellLayer(wellId = 1, rockTypeId = 7, startPoint = 3312, endPoint = 3839),
                    WellLayer(wellId = 1, rockTypeId = 1, startPoint = 3839, endPoint = 4450),

                    WellLayer(wellId = 2, rockTypeId = 9, startPoint = 0, endPoint = 755),
                    WellLayer(wellId = 2, rockTypeId = 11, startPoint = 755, endPoint = 1523),
                    WellLayer(wellId = 2, rockTypeId = 3, startPoint = 1523, endPoint = 2280),
                    WellLayer(wellId = 2, rockTypeId = 6, startPoint = 2280, endPoint = 2916),
                    WellLayer(wellId = 2, rockTypeId = 6, startPoint = 2280, endPoint = 2916),
                    WellLayer(wellId = 2, rockTypeId = 10, startPoint = 2916, endPoint = 3727),
                    WellLayer(wellId = 2, rockTypeId = 1, startPoint = 3727, endPoint = 4230),

                    WellLayer(wellId = 3, rockTypeId = 10, startPoint = 0, endPoint = 808),
                    WellLayer(wellId = 3, rockTypeId = 5, startPoint = 808, endPoint = 1605),
                    WellLayer(wellId = 3, rockTypeId = 1, startPoint = 1605, endPoint = 2129),
                    WellLayer(wellId = 3, rockTypeId = 6, startPoint = 2129, endPoint = 2770),
                    WellLayer(wellId = 3, rockTypeId = 9, startPoint = 2770, endPoint = 3738),
                    WellLayer(wellId = 3, rockTypeId = 8, startPoint = 3738, endPoint = 4670),
                    WellLayer(wellId = 3, rockTypeId = 4, startPoint = 4670, endPoint = 4830),
                )
            }
        }
    }
}
fun checkInternet(onFail:()->Unit,onOk:()->Unit){
    val client = OkHttpClient()
        .newBuilder()
        .build()
    val request = Request.Builder()
        .get()
        .url("https://google.com")
        .build()

    try {
        client.newCall(request).execute()
        onOk()
    }    catch (e:Exception){
        onFail()
    }
}
