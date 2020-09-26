package br.com.anderson.cocuscodechallenge.persistence

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.cocuscodechallenge.model.User
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class UserDaoTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CodeWarsDb

    @Before fun createDb() {
         database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(),
                 CodeWarsDb::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun `test empty db`() {
        database.codeWarsDao().allUsers()
                .test()
                .assertValue { it.isEmpty() }
    }

    @Test fun `test insert and get user`() {

        database.codeWarsDao().insertUser(USER).blockingAwait()

        database.codeWarsDao().allUsers()
                .test()
                .assertValue { it.find { f-> f.username == "username" } != null }
    }

    @Test fun `test update and get user`() {
        database.codeWarsDao().insertUser(USER).blockingAwait()

        val datetime = 957150000000L

        val updatedUser = USER.copy(datetime = datetime)
        database.codeWarsDao().insertUser(updatedUser).blockingAwait()

        database.codeWarsDao().allUsers()
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue { it.find { f->  f.datetime  == datetime} != null }
    }


    companion object {
        private val USER = User(datetime = 946692000000L,username = "username",name = "Name", clan = "clean")
    }
}
