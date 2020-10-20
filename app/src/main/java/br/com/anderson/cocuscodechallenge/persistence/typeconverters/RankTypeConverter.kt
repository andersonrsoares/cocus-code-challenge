package br.com.anderson.cocuscodechallenge.persistence.typeconverters

import androidx.room.TypeConverter
import br.com.anderson.cocuscodechallenge.model.Rank
import com.google.gson.Gson

class RankTypeConverter {

    @TypeConverter
    fun rankToString(rank: Rank?): String {
        if (rank == null)
            return ""

        return try { Gson().toJson(rank) } catch (e: Exception) { "" }
    }

    @TypeConverter
    fun stringToRank(data: String?): Rank? {
        if (data.isNullOrBlank())
            return null

        return try { Gson().fromJson<Rank>(data, Rank::class.java) } catch (e: Exception) { null }
    }
}
