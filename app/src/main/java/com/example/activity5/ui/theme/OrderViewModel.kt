package com.example.activity5.ui.theme

import androidx.lifecycle.ViewModel
import com.example.activity5.Data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat

private  const val  HARGA_PER_CUP = 3000

class OrderViewModel : ViewModel() {
    private val _stateUi = MutableStateFlow(OrderUiState())
    val stateUi : StateFlow<OrderUiState> = _stateUi.asStateFlow()


    fun setCostumer(nama: String, nomor: String, alamat:String){
        _stateUi.update { stateSaatIni ->
            stateSaatIni.copy(
                namaCostumer = nama,
                noHP = nomor,
                alamat = alamat
            )
        }
    }
    fun setJumlah(jmlEsJumbo: Int) {
        _stateUi.update { stateSaatIni ->
            stateSaatIni.copy(
                jumlah = jmlEsJumbo,
                harga = hitungHarga(jumlah = jmlEsJumbo)
            )
        }
    }

    fun setRasa(rasaPilihan: String) {
        _stateUi.update { stateSaatIni ->
            stateSaatIni.copy(rasa = rasaPilihan)
        }
    }

    fun resetOrder() {
        _stateUi.value = OrderUiState()
    }

    private fun hitungHarga(
        jumlah: Int = _stateUi.value.jumlah,
    ): String{
        val kalkulasiHarga = jumlah * HARGA_PER_CUP

        return NumberFormat.getNumberInstance().format(kalkulasiHarga)
    }
}