package br.com.anderson.cocuscodechallenge.persistence.typeconverters

import androidx.room.TypeConverter
import br.com.anderson.cocuscodechallenge.model.CodeChallenges
import com.google.gson.Gson

class CodeChallengesTypeConverter {

    @TypeConverter
    fun codeChallengesToString(codeChallenges: CodeChallenges?): String {
        if(codeChallenges == null)
            return ""

        return try { Gson().toJson(codeChallenges) } catch (e:Exception) { "" }
    }

    @TypeConverter
    fun stringToCodeChallenges(data: String?): CodeChallenges? {
        if(data.isNullOrBlank())
            return null

        return try { Gson().fromJson<CodeChallenges>(data,CodeChallenges::class.java) } catch (e:Exception) { null }
    }
}