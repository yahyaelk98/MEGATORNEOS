package com.megatorneos.model

import android.net.Uri
import java.util.*

data class Chat(
    var id: String = "",
    var message:String = "",
    var messageDate: Date? = null,
    var sentBy: String = "",
    var avatar: Uri? = null
)