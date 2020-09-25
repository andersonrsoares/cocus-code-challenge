package br.com.anderson.cocuscodechallenge.extras

import br.com.anderson.cocuscodechallenge.vo.Language
import br.com.anderson.cocuscodechallenge.vo.Languages
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class LanguageDeserializer : JsonDeserializer<Languages> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Languages {
        val languages = Languages()
        json?.asJsonObject?.let { obj ->
            languages.language = hashMapOf()
            obj.entrySet(). forEach {
                languages.language?.put(it.key,Gson().fromJson(it.value, Language::class.java))
            }
        }
        return languages
    }
}