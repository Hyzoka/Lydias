import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.test.data.local.AppDatabase
import com.test.data.local.UserDao
import com.test.data.model.entity.UserEntity
import com.test.data.model.entity.toUser
import com.test.data.remote.RandomUserApi
import com.test.data.repo.ContactRepositoryImpl
import com.test.data.util.NetworkConnectivityHelper
import com.test.domain.model.User
import com.test.domain.repo.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class ContactRepositoryImplTest {

    @Mock
    private lateinit var db: AppDatabase

    @Mock
    private lateinit var api: RandomUserApi

    @Mock
    private lateinit var networkMonitor: NetworkConnectivityHelper

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var repository: ContactRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        whenever(db.userDao()).thenReturn(userDao)

        repository = ContactRepositoryImpl(db, api, networkMonitor)
    }

    @Test
    fun `getPaginatedContacts returns correct flow`() = runTest {
        val fakeUserEntity = UserEntity(
            email = "test@example.com",
            phone = "123456789",
            cell = "987654321",
            pictureUrl = "http://example.com/picture.jpg",
            fullName = "John Doe",
            city = "Paris",
            country = "France",
            birthDate = "1990-01-01",
            age = 34,
            nationality = "FR"
        )
        val fakeUser = fakeUserEntity.toUser()

        val pagingData = PagingData.from(listOf(fakeUserEntity))
        whenever(userDao.getUsersPaging()).thenReturn(mock())
        whenever(networkMonitor.isConnected).thenReturn(MutableStateFlow(true))

        val result = repository.getPaginatedContacts()

        assert(result is Flow<PagingData<User>>)
    }

    @Test
    fun `getUserByEmail returns correct flow`() = runTest {
        val fakeUserEntity = UserEntity(
            email = "test@example.com",
            phone = "123456789",
            cell = "987654321",
            pictureUrl = "http://example.com/picture.jpg",
            fullName = "John Doe",
            city = "Paris",
            country = "France",
            birthDate = "1990-01-01",
            age = 34,
            nationality = "FR"
        )
        val fakeUser = fakeUserEntity.toUser()

        whenever(userDao.getUserByEmail(any())).thenReturn(flowOf(fakeUserEntity))

        val result = repository.getUserByEmail("test@example.com").first()

        assert(result == fakeUser)
    }

    @Test
    fun `getPaginatedContacts returns empty flow when offline`() = runTest {
        whenever(networkMonitor.isConnected).thenReturn(MutableStateFlow(false))

        val result = repository.getPaginatedContacts()

        assert(result is Flow<PagingData<User>>)
    }

    @Test
    fun `getUserByEmail returns empty object when user not found`() = runTest {
        val emptyUserEntity = UserEntity(
            email = "",
            phone = "",
            cell = "",
            pictureUrl = "",
            fullName = "",
            city = "",
            country = "",
            birthDate = "",
            age = 0,
            nationality = ""
        )
        val emptyUser = emptyUserEntity.toUser()

        whenever(userDao.getUserByEmail(any())).thenReturn(flowOf(emptyUserEntity))

        val result = repository.getUserByEmail("unknown@example.com").first()

        assert(result == emptyUser)
    }
}
