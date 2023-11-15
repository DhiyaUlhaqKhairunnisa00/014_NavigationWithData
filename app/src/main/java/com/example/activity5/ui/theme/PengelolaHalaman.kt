@file:OptIn(ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class)

package com.example.activity5.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.activity5.Data.SumberData.flavors
import com.example.activity5.R

enum class PengelolaHalaman {
    Home,
    Rasa,
    Summary,
    Costumer,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsJumboAppBar(
    bisaNavigasiBack: Boolean,
    navigasiUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text (stringResource(id = R.string.app_name))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (bisaNavigasiBack){
                IconButton(onClick = navigasiUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun EsJumboApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    Scaffold(
        topBar = {
            EsJumboAppBar(
                bisaNavigasiBack = false,
                navigasiUp = {})
        }
    ){
        innerPadding ->
        val uiState by viewModel.stateUi.collectAsState()
        NavHost(
            navController = navController,
            startDestination = PengelolaHalaman.Home.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = PengelolaHalaman.Home.name)
            {
                HalamanHome(
                    onNextButtonClicked = {
                        navController.navigate(PengelolaHalaman.Costumer.name) })
            }
            composable(route = PengelolaHalaman.Costumer.name){
                Costumer(
                    onConfirmButtonClicked = { nama, nomor, alamat ->
                        viewModel.setCostumer(nama, nomor, alamat)
                        navController.navigate(PengelolaHalaman.Rasa.name)
                    },
                    onCancelButtonClicked = {
                        navController.navigate(PengelolaHalaman.Home.name)
                    },
                    )
            }
            composable(route =PengelolaHalaman.Rasa.name) {
                val context = LocalContext.current
                HalamanSatu(
                    pilihanRasa = flavors. map { id -> context.resources.getString(id)},
                    onSelectionChanged = { viewModel.setRasa(it)},
                    onConfirmButtonClicked = {viewModel.setJumlah(it)},
                    onNextButtonClicked = { navController.navigate(PengelolaHalaman.Summary.name) },
                    onCancelButtonClicked = { navController.navigate(PengelolaHalaman.Costumer.name) })
            }
            composable(route = PengelolaHalaman.Summary.name){
                HalamanDua(
                    orderUiState = uiState,
                    onCancelButtonClicked = {cancelOrderAndNavigateToRasa(navController)},
                    )
            }
        }
    }
}

private fun cancelOrderAndNavigateToHome(
    viewModel: OrderViewModel,
    navController: NavHostController
){
    viewModel.resetOrder()
    navController.popBackStack(PengelolaHalaman.Home.name, inclusive = false)
}
private fun cancelOrderAndNavigateToRasa(
    navController: NavHostController
){
    navController.popBackStack(PengelolaHalaman.Rasa.name, inclusive = false)
}

