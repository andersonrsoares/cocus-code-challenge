package br.com.anderson.cocuscodechallenge.persistence


import android.os.Build
import androidx.room.EmptyResultSetException
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.model.AuthoredChallenge
import br.com.anderson.cocuscodechallenge.model.Challenge
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ChallengeDaoTest: BaseDaoTest() {


    @Test fun `test empty db challenge`() {
        database.codeWarsDao().getChallenge("id")
                .test()
                .assertNoValues()
    }

    @Test fun `test insert and get challenge`() {

        database.codeWarsDao().insertChallenge(CHALLENGE).blockingAwait()

        database.codeWarsDao().getChallenge("id")
                .test()
                .assertValue { it == CHALLENGE }
    }

    @Test fun `test update and get challenge`() {
        database.codeWarsDao().insertChallenge(CHALLENGE).blockingAwait()

        val datetime = 957150000000L

        val updatedUser = CHALLENGE.copy(publishedAt = datetime)
        database.codeWarsDao().insertChallenge(updatedUser).blockingAwait()

        database.codeWarsDao().getChallenge("id")
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue {  it.publishedAt == datetime }
    }


    companion object {
        private val CHALLENGE = Challenge(name = "Name", description = "description", id = "id", languages = arrayListOf("javascript"), tags = arrayListOf("tag1"))
    }
}
