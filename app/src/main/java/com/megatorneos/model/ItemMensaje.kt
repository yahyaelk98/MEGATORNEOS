package com.megatorneos.model

import android.net.Uri

data class ItemMensaje(
    var id: String = "",
    var titulo: String = "",
    var ultimoMensaje: String = "",
    var fechaUltimoMensaje: String = "",
    var image: Uri? = null
)