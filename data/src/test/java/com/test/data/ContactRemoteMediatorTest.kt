package com.test.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.data.local.AppDatabase
import com.test.data.local.RemoteKeysDao
import com.test.data.local.UserDao
import com.test.data.mediator.ContactRemoteMediator
import com.test.data.model.dto.ContactResponseDto
import com.test.data.model.dto.DobDto
import com.test.data.model.dto.LocationDto
import com.test.data.model.dto.LoginDto
import com.test.data.model.dto.NameDto
import com.test.data.model.dto.PageInfoDto
import com.test.data.model.dto.PictureDto
import com.test.data.model.dto.UserDto
import com.test.data.model.dto.toEntity
import com.test.data.model.entity.RemoteKeys
import com.test.data.model.entity.UserEntity
import com.test.data.remote.RandomUserApi
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Method

@OptIn(ExperimentalPagingApi::class)
class ContactRemoteMediatorTest {

    @MockK
    private lateinit var database: AppDatabase

    @MockK
    private lateinit var api: RandomUserApi

    @MockK
    private lateinit var userDao: UserDao

    @MockK
    private lateinit var remoteKeysDao: RemoteKeysDao

    private lateinit var mediator: ContactRemoteMediator

    private val mockUserEntity = UserEntity(
        email = "test@example.com",
        phone = "123456789",
        cell = "987654321",
        pictureUrl = "https://example.com/pic.jpg",
        fullName = "Test User",
        city = "Test City",
        country = "Test Country",
        birthDate = "1990-01-01",
        age = 33,
        nationality = "Test"
    )

    private val mockRemoteKey = RemoteKeys(
        contactEmail = "test@example.com",
        prevKey = 1,
        currentPage = 2,
        nextKey = 3
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { database.userDao() } returns userDao
        every { database.getRemoteKeysDao() } returns remoteKeysDao

        mediator = ContactRemoteMediator(database, api, isOnline = true)
    }

    // Tests pour la logique de détermination de page dans load()

    @Test
    fun `load with REFRESH and no remote keys should start at page 1`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail(any()) } returns null
        setupSuccessfulApiCall(page = 1)

        // When
        val result = mediator.load(LoadType.REFRESH, pagingState)

        // Then
        coVerify { api.fetchContacts(page = 1, results = any()) }
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load with REFRESH and existing remote key should calculate correct page`() = runTest {
        // Given
        val remoteKey = mockRemoteKey.copy(nextKey = 5)
        val pagingState = createPagingStateWithAnchor()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail("test@example.com") } returns remoteKey
        setupSuccessfulApiCall(page = 4) // nextKey - 1

        // When
        val result = mediator.load(LoadType.REFRESH, pagingState)

        // Then
        coVerify { api.fetchContacts(page = 4, results = any()) }
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load with APPEND and valid nextKey should load next page`() = runTest {
        // Given
        val remoteKey = mockRemoteKey.copy(nextKey = 5)
        val pagingState = createPagingStateWithData()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail("test@example.com") } returns remoteKey
        setupSuccessfulApiCall(page = 5)

        // When
        val result = mediator.load(LoadType.APPEND, pagingState)

        // Then
        coVerify { api.fetchContacts(page = 5, results = any()) }
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load with APPEND and null nextKey should return endOfPaginationReached`() = runTest {
        // Given
        val remoteKey = mockRemoteKey.copy(nextKey = null)
        val pagingState = createPagingStateWithData()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail("test@example.com") } returns remoteKey

        // When
        val result = mediator.load(LoadType.APPEND, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { api.fetchContacts(any(), any(), any()) }
    }

    @Test
    fun `load with PREPEND and valid prevKey should load previous page`() = runTest {
        // Given
        val remoteKey = mockRemoteKey.copy(prevKey = 3)
        val pagingState = createPagingStateWithData()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail("test@example.com") } returns remoteKey
        setupSuccessfulApiCall(page = 3)

        // When
        val result = mediator.load(LoadType.PREPEND, pagingState)

        // Then
        coVerify { api.fetchContacts(page = 3, results = any()) }
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `load with PREPEND and null prevKey should return endOfPaginationReached`() = runTest {
        // Given
        val remoteKey = mockRemoteKey.copy(prevKey = null)
        val pagingState = createPagingStateWithData()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail("test@example.com") } returns remoteKey

        // When
        val result = mediator.load(LoadType.PREPEND, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { api.fetchContacts(any(), any(), any()) }
    }

    // Tests pour la logique métier spécifique

    @Test
    fun `load when offline should return Success with endOfPaginationReached true`() = runTest {
        // Given
        val offlineMediator = ContactRemoteMediator(database, api, isOnline = false)
        val pagingState = createEmptyPagingState()

        // When
        val result = offlineMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { api.fetchContacts(any(), any(), any()) }
    }

    @Test
    fun `load with REFRESH should clear database before inserting new data`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()
        setupSuccessfulApiCall(page = 1)

        // When
        mediator.load(LoadType.REFRESH, pagingState)

        // Then
        coVerifyOrder {
            database.withTransaction(any<suspend () -> Unit>())
            remoteKeysDao.clearRemoteKeys()
            userDao.clearAll()
            remoteKeysDao.insertAll(any())
            userDao.insertAll(any())
        }
    }

    @Test
    fun `load with APPEND should not clear database`() = runTest {
        // Given
        val pagingState = createPagingStateWithData()
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail(any()) } returns mockRemoteKey
        setupSuccessfulApiCall(page = 3)

        // When
        mediator.load(LoadType.APPEND, pagingState)

        // Then
        coVerify(exactly = 0) { remoteKeysDao.clearRemoteKeys() }
        coVerify(exactly = 0) { userDao.clearAll() }
        coVerify { remoteKeysDao.insertAll(any()) }
        coVerify { userDao.insertAll(any()) }
    }

    @Test
    fun `load should calculate correct prevKey and nextKey for remote keys`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()
        val page = 3
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail(any()) } returns null
        setupSuccessfulApiCall(page = page)

        // When
        mediator.load(LoadType.REFRESH, pagingState)

        // Then
        coVerify {
            remoteKeysDao.insertAll(match { keys ->
                keys.isNotEmpty() &&
                        keys.first().prevKey == 2 && // page - 1
                        keys.first().nextKey == 4 && // page + 1
                        keys.first().currentPage == 3
            })
        }
    }

    @Test
    fun `load should set prevKey to null when page is 1`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()
        setupSuccessfulApiCall(page = 1)

        // When
        mediator.load(LoadType.REFRESH, pagingState)

        // Then
        coVerify {
            remoteKeysDao.insertAll(match { keys ->
                keys.isNotEmpty() &&
                        keys.first().prevKey == null &&
                        keys.first().currentPage == 1
            })
        }
    }

    @Test
    fun `load should return endOfPaginationReached true when API returns empty results`() =
        runTest {
            // Given
            val pagingState = createEmptyPagingState()
            val emptyResponse = ContactResponseDto(
                results = emptyList(),
                info = PageInfoDto(page = 2, results = 0, seed = "test")
            )
            coEvery { api.fetchContacts(any(), any(), any()) } returns emptyResponse
            coEvery { remoteKeysDao.getRemoteKeyByContactEmail(any()) } returns null
            setupDatabaseMocks()

            // When
            val result = mediator.load(LoadType.REFRESH, pagingState)

            // Then
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun `load should return endOfPaginationReached true when API returns null`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()
        coEvery { api.fetchContacts(any(), any(), any()) } returns null
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail(any()) } returns null
        setupDatabaseMocks()

        // When
        val result = mediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `load should return Error when API throws exception`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()
        val exception = RuntimeException("Network error")
        coEvery { remoteKeysDao.getRemoteKeyByContactEmail(any()) } returns null
        coEvery { api.fetchContacts(any(), any(), any()) } throws exception

        // When
        val result = mediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(exception, (result as RemoteMediator.MediatorResult.Error).throwable)
    }

    // Tests pour les méthodes privées (en utilisant la réflexion pour les tester)

    @Test
    fun `getRemoteKeyClosestToCurrentPosition should return null when anchorPosition is null`() =
        runTest {
            // Given
            val pagingState = PagingState<Int, UserEntity>(
                pages = emptyList(),
                anchorPosition = null,
                config = PagingConfig(pageSize = 20),
                leadingPlaceholderCount = 0
            )

            // When
            val result = callPrivateMethod("getRemoteKeyClosestToCurrentPosition", pagingState)

            // Then
            assertNull(result)
        }

    @Test
    fun `getRemoteKeyForFirstItem should return null when pages are empty`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()

        // When
        val result = callPrivateMethod("getRemoteKeyForFirstItem", pagingState)

        // Then
        assertNull(result)
    }

    @Test
    fun `getRemoteKeyForLastItem should return null when pages are empty`() = runTest {
        // Given
        val pagingState = createEmptyPagingState()

        // When
        val result = callPrivateMethod("getRemoteKeyForLastItem", pagingState)

        // Then
        assertNull(result)
    }

    // Méthodes utilitaires

    private fun createEmptyPagingState(): PagingState<Int, UserEntity> {
        return PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )
    }

    private fun createPagingStateWithAnchor(): PagingState<Int, UserEntity> {
        val page = PagingSource.LoadResult.Page(
            data = listOf(mockUserEntity),
            prevKey = 1,
            nextKey = 3
        )

        return PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )
    }

    private fun createPagingStateWithData(): PagingState<Int, UserEntity> {
        val page = PagingSource.LoadResult.Page(
            data = listOf(mockUserEntity),
            prevKey = 1,
            nextKey = 3
        )

        return PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )
    }

    private fun setupSuccessfulApiCall(page: Int) {
        val mockUserDto = createFakeUserDto()
        val mockResponse = ContactResponseDto(
            results = listOf(mockUserDto),
            info = PageInfoDto(page = page, results = 1, seed = "test")
        )

        coEvery { api.fetchContacts(page = page, results = any()) } returns mockResponse

        // Mock de la fonction d'extension toEntity()
        mockkStatic("com.test.data.model.dto.UserDtoKt")
        every { mockUserDto.toEntity() } returns mockUserEntity

        setupDatabaseMocks()
    }

    private fun setupDatabaseMocks() {
        coEvery { database.withTransaction(any<suspend () -> Unit>()) } coAnswers {
            firstArg<suspend () -> Unit>().invoke()
        }
        coEvery { remoteKeysDao.clearRemoteKeys() } just Runs
        coEvery { userDao.clearAll() } just Runs
        coEvery { remoteKeysDao.insertAll(any()) } just Runs
        coEvery { userDao.insertAll(any()) } just Runs
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun callPrivateMethod(methodName: String, vararg args: Any): Any? {
        val method: Method = ContactRemoteMediator::class.java.getDeclaredMethod(
            methodName,
            PagingState::class.java
        )
        method.isAccessible = true
        return method.invoke(mediator, *args) as? RemoteKeys
    }
}

private fun createFakeUserDto(): UserDto {
    return UserDto(
        email = "john.doe@example.com",
        phone = "123456789",
        cell = "987654321",
        picture = PictureDto("thumb.jpg", "medium.jpg", "large.jpg"),
        name = NameDto("Mr", "John", "Doe"),
        location = LocationDto("Paris", "France"),
        dob = DobDto("1990-01-01", 34),
        nationality = "FR",
        login = LoginDto("uuid-1234")
    )
}