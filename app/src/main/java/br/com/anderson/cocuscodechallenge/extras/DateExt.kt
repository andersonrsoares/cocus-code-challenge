package br.com.anderson.cocuscodechallenge.extras

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


fun String?.toTimestamp():Long{
    return try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this).time
    }catch (e:Exception){
         0
    }
}

fun Long?.toTimestamp():String{
    return try {
        SimpleDateFormat("yyyy-MM-dd").format(Date(this ?: 0))
    }catch (e:Exception){
        ""
    }
}