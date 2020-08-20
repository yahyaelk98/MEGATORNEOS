package com.megatorneos.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun showToast(context: Context, text: String) =
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

fun showSnackBarClose(contextView: View, text: String) {
    Snackbar.make(contextView, text, Snackbar.LENGTH_LONG)
        .setAction("CERRAR") {}.show()
}

fun showSnackBar(contextView: View, text: String) =
    Snackbar.make(contextView, text, Snackbar.LENGTH_LONG).show()

fun dateToStringMensajes(fecha: Date) : String{
    val cal = Calendar.getInstance().apply {
        time = fecha
    }

    var devolver = cal[Calendar.DAY_OF_MONTH].toString() + " "
    when(cal[Calendar.MONTH]){
        0 -> devolver += "ene. "
        1 -> devolver += "feb. "
        2 -> devolver += "mar. "
        3 -> devolver += "abr. "
        4 -> devolver += "may. "
        5 -> devolver += "jun. "
        6 -> devolver += "jul. "
        7 -> devolver += "ago. "
        8 -> devolver += "sep. "
        9 -> devolver += "oct. "
        10 -> devolver += "nov. "
        11 -> devolver += "dic. "
    }
    devolver += cal[Calendar.YEAR]
    return devolver
}

fun dpToPx(dp: Int) = dp * Resources.getSystem().displayMetrics.density

fun pxToDp(px: Int) = px * Resources.getSystem().displayMetrics.density

fun dateToTimeString(fecha: Date): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val parsedDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        parsedDate.format(DateTimeFormatter.ofPattern("hh:mma")).toLowerCase()
    } else {
        SimpleDateFormat("hh:mma").format(fecha).toLowerCase()
    }

}

fun checkMismoDia(fecha1: Date, fecha2: Date): Boolean{
    val cal1 = Calendar.getInstance().apply {
        time = fecha1
    }
    val cal2 = Calendar.getInstance().apply {
        time = fecha2
    }
    return cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
            cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
}