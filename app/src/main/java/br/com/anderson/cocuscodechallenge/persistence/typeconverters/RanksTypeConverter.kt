package br.com.anderson.cocuscodechallenge.persistence.typeconverters

import androidx.room.TypeConverter
import br.com.anderson.cocuscodechallenge.model.Ranks
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
        if(data.isNullOrBlank())
            return null

        return try { Gson().fromJson<Ranks>(data, Ranks::class.java) } catch (e:Exception) { null }
    }
}