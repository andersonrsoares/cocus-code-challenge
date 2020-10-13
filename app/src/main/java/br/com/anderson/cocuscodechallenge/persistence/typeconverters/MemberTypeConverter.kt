package br.com.anderson.cocuscodechallenge.persistence.typeconverters

import androidx.room.TypeConverter
import br.com.anderson.cocuscodechallenge.model.Member
import com.google.gson.Gson

class MemberTypeConverter {

    @TypeConverter
    fun memberToString(member: Member?): String {
        if (member == null)
            return ""

        return try { Gson().toJson(member) } catch (e: Exception) { "" }
    }

    @TypeConverter
    fun stringToMember(data: String?): Member? {
        if (data.isNullOrBlank())
            return null

        return try { Gson().fromJson<Member>(data, Member::class.java) } catch (e: Exception) { null }
    }
}
