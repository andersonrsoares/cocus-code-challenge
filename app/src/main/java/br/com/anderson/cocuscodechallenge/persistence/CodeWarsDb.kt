package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.*
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import br.com.anderson.cocuscodechallenge.vo.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)

abstract class CodeWarsDb : RoomDatabase() {
    abstract fun connectedCommunitiesDao(): CodeWarsDao

}





