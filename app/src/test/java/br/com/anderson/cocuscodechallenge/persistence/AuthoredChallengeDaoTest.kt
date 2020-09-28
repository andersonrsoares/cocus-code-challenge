package br.com.anderson.cocuscodechallenge.persistence


import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AuthoredChallengeDaoTest: BaseDaoTest() {


    @Test fun `test empty db`() {
        database.codeWarsDao().allAuthoredChallenges("username")
                .test()
                .assertValue { it.isEmpty() }
    }

    @Test fun `test insert and get challenge`() {

        database.codeWarsDao().insertAuthoredChallenge(CHALLENGE).blockingAwait()

        database.codeWarsDao().allAuthoredChallenges("username")
                .test()
                .assertValue { it.contains(CHALLENGE) }
    }

    @Test fun `test update and get challenge`() {
        database.codeWarsDao().insertAuthoredChallenge(CHALLENGE).blockingAwait()

        val rank = 101

        val updatedUser = CHALLENGE.copy(rank = rank)
        database.codeWarsDao().insertAuthoredChallenge(updatedUser).blockingAwait()

        database.codeWarsDao().allAuthoredChallenges("username")
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue { it.find { f->  f.rank  == 101} != null }
    }


    companion object {
        private val CHALLENGE = AuthoredChallenge(username = "username",name = "Name", description = "description", id = "id", languages = arrayListOf("javascript"), rank = 1, tags = arrayListOf("tag1"))
    }
}
