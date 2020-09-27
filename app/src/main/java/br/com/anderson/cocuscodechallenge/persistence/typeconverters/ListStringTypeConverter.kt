package br.com.anderson.cocuscodechallenge.persistence.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringTypeConverter {

    @TypeConverter
    fun listToString(list: List<String>?): String {
        if(list == null)
            return ""

        return try { Gson().toJson(list) } catch (e:Exception) { "" }
    }

    @TypeConverter
    fun stringToList(data: String?): List<String>? {
        if(data.isNullOrBlank())
            return null

        return try { Gson().fromJson<List<String>>(data,
            object : TypeToken<List<String>>() {}.type) } catch (e:Exception) { null }
    }
}