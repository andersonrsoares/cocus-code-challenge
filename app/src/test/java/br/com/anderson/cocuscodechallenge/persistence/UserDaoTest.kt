package br.com.anderson.cocuscodechallenge.persistence

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.model.User
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class UserDaoTest : BaseDaoTest() {

    @Test fun `test empty db`() {
        database.codeWarsDao().allUsers()
            .test()
            .assertValue { it.isEmpty() }
    }

    @Test fun `test insert and get user`() {

        database.codeWarsDao().insertUser(USER).blockingAwait()

        database.codeWarsDao().allUsers()
            .test()
            .assertValue { it.find { f -> f.username == "username" } != null }
    }

    @Test fun `test update and get user`() {
        database.codeWarsDao().insertUser(USER).blockingAwait()

        val datetime = 957150000000L

        val updatedUser = USER.copy(datetime = datetime)
        database.codeWarsDao().insertUser(updatedUser).blockingAwait()

        database.codeWarsDao().allUsers()
            .test()
            // assertValue asserts that there was only one emission of the user
            .assertValue { it.find { f -> f.datetime == datetime } != null }
    }

    companion object {
        private val USER = User(datetime = 946692000000L, username = "username", name = "Name", clan = "clean")
    }
}
