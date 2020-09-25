package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.TypeConverter
import br.com.anderson.cocuscodechallenge.vo.CodeChallenges
import com.google.gson.Gson

object UserTypeConverters {

    @TypeConverter
    @JvmStatic
    fun codeChallengesToString(codeChallenges: CodeChallenges?): String {
        if(codeChallenges == null)
            return ""

        return try { Gson().toJson(codeChallenges) } catch (e:Exception) { "" }
    }

    @TypeConverter
    @JvmStatic
    fun stringToCodeChallenges(data: String?): CodeChallenges? {
        if(data == null || data.isBlank())
            return null

        return try { Gson().fromJson<CodeChallenges>(data,CodeChallenges::class.java::class.java) } catch (e:Exception) { null }
    }


}