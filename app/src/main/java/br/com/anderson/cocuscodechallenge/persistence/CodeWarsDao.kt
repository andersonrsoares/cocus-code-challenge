package br.com.anderson.cocuscodechallenge.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.Challenge
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import br.com.anderson.cocuscodechallenge.model.User
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
@OpenForTesting
abstract class CodeWarsDao {

    @Query("SELECT * from User order by datetime desc limit 5")
    abstract fun allUsers(): Single<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: User): Completable

    @Query("SELECT * from CompletedChallenge where username == :username order by completedAt desc limit 200")
    abstract fun allCompletedChallenges(username: String): Single<List<CompletedChallenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCompletedChallenge(completedChallenge: CompletedChallenge): Completable

    @Query("SELECT * from AuthoredChallenge where username == :username")
    abstract fun allAuthoredChallenges(username: String): Single<List<AuthoredChallenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAuthoredChallenge(authoredChallenge: AuthoredChallenge): Completable

    @Query("SELECT * from Challenge where id == :id")
    abstract fun getChallenge(id: String): Maybe<Challenge>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertChallenge(challenge: Challenge): Completable
}
