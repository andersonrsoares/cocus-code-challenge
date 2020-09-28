package br.com.anderson.cocuscodechallenge.extras

import android.annotation.SuppressLint
import java.text.SimpleDateFormat


fun String?.toTimestamp():Long{
    return try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z").parse(this).time
    }catch (e:Exception){
         0
    }
}