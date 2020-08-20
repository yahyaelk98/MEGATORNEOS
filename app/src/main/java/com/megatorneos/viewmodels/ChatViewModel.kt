package com.megatorneos.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.megatorneos.model.Chat

class ChatViewModel : ViewModel() {
    val image = MutableLiveData<Uri>()
    val titulo = MutableLiveData<String>().apply { value = "" }
    val fecha = MutableLiveData<String>().apply { value = "" }
    val listChats = MutableLiveData<MutableList<Chat>>().apply { value = ArrayList() }

    fun addChat(itemChat: Chat){
        listChats.value?.add(itemChat)
        listChats.value = listChats.value
    }

}