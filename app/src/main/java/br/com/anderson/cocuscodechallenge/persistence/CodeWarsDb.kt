package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.*
import br.com.anderson.cocuscodechallenge.model.User

@Database(
    entities = [User::class],
    version = 2,
    exportSchema = false
)

abstract class CodeWarsDb : RoomDatabase() {
    abstract fun codeWarsDao(): CodeWarsDao

}





