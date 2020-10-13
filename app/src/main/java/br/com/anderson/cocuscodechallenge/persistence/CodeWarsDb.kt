package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User

@Database(
    entities = [User::class, CompletedChallenge::class, AuthoredChallenge::class, Challenge::class],
    version = 8,
    exportSchema = false
)

abstract class CodeWarsDb : RoomDatabase() {
    abstract fun codeWarsDao(): CodeWarsDao
}
