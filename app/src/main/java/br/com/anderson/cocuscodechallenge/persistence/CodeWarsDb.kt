package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import br.com.anderson.cocuscodechallenge.vo.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)

abstract class CodeWarsDb : RoomDatabase() {
    abstract fun connectedCommunitiesDao(): CodeWarsDao

}

@Dao
@OpenForTesting
abstract class CodeWarsDao {

    @Query("SELECT * from user")
    abstract fun allUsers(): List<User>
}



