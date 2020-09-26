package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import br.com.anderson.cocuscodechallenge.model.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
@OpenForTesting
abstract class CodeWarsDao {

    @Query("SELECT * from User order by datetime desc limit 5")
    abstract fun allUsers(): Flowable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: User): Completable
}