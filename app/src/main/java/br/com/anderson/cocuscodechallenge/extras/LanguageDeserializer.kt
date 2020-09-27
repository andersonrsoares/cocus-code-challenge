package br.com.anderson.cocuscodechallenge.extras

import br.com.anderson.cocuscodechallenge.model.Language
import br.com.anderson.cocuscodechallenge.model.Languages
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
        var languages:  ArrayList<Language>? = null
        json?.asJsonObject?.let { obj ->
            languages  = arrayListOf()
            obj.entrySet().forEach {
                languages?.add(Gson().fromJson(it.value, Language::class.java)
                    .apply { languageName = it.key })
            }
        }
        return Languages(languages)
    }
}