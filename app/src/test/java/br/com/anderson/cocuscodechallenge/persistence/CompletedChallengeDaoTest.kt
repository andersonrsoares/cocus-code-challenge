package br.com.anderson.cocuscodechallenge.persistence


import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.model.CompletedChallenge
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CompletedChallengeDaoTest: BaseDaoTest() {


    @Test fun `test empty db`() {
        database.codeWarsDao().allCompletedChallenges("username")
                .test()
                .assertValue { it.isEmpty() }
    }

    @Test fun `test insert and get challenge`() {

        database.codeWarsDao().insertCompletedChallenge(CHALLENGE).blockingAwait()

        database.codeWarsDao().allCompletedChallenges("username")
                .test()
                .assertValue { it.contains(CHALLENGE) }
    }

    @Test fun `test update and get challenge`() {
        database.codeWarsDao().insertCompletedChallenge(CHALLENGE).blockingAwait()

        val datetime = 957150000000L

        val updatedUser = CHALLENGE.copy(completedAt = datetime)
        database.codeWarsDao().insertCompletedChallenge(updatedUser).blockingAwait()

        database.codeWarsDao().allCompletedChallenges("username")
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue { it.find { f->  f.completedAt  == datetime} != null }
    }


    companion object {
        private val CHALLENGE = CompletedChallenge(completedAt = 946692000000L,username = "username",name = "Name", slug = "slog", id = "id", completedLanguages = arrayListOf("javascript"))
    }
}
