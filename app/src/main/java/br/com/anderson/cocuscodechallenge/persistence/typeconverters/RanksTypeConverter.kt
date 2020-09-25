package br.com.anderson.cocuscodechallenge.persistence.typeconverters

import androidx.room.TypeConverter
import br.com.anderson.cocuscodechallenge.vo.Ranks
import com.google.gson.Gson

class RanksTypeConverter {

    @TypeConverter
    fun ranksToString(ranks: Ranks?): String {
        if(ranks == null)
            return ""

        return try { Gson().toJson(ranks) } catch (e:Exception) { "" }
    }

    @TypeConverter
    fun stringToRanks(data: String?): Ranks? {
        if(data == null || data.isBlank())
            return null

        return try { Gson().fromJson<Ranks>(data, Ranks::class.java::class.java) } catch (e:Exception) { null }
    }
}