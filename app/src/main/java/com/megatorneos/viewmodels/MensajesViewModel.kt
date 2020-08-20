package com.megatorneos.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.megatorneos.model.ItemMensaje

class MensajesViewModel : ViewModel() {
    val listMensajes = MutableLiveData<MutableList<ItemMensaje>>().apply { value = ArrayList() }

    fun setListMensajes(listMensaje: MutableList<ItemMensaje>) {
        listMensajes.value = listMensaje
    }
    fun getListMensajes() = listMensajes.value

    fun addMensaje(mensaje: ItemMensaje){
        listMensajes.value?.add(mensaje)
        listMensajes.value = listMensajes.value
    }

}