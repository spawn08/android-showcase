package com.igorwojda.showcase.feature.album.domain.usecase

import com.igorwojda.showcase.feature.album.data.AlbumRepositoryImpl
import com.igorwojda.showcase.feature.album.domain.DomainFixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.UnknownHostException

class GetAlbumListUseCaseTest {

    @MockK
    internal lateinit var mockAlbumRepository: AlbumRepositoryImpl

    private lateinit var cut: GetAlbumListUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        cut = GetAlbumListUseCase(mockAlbumRepository)
    }

    @Test
    fun `return list of albums`() {
        // given
        val albums = listOf(DomainFixtures.getAlbum(), DomainFixtures.getAlbum())
        coEvery { mockAlbumRepository.searchAlbum(any()) } returns albums

        // when
        val result = runBlocking { cut.execute() }

        // then
        result shouldBeEqualTo GetAlbumListUseCase.Result.Success(albums)
    }

    @Test
    fun `filter albums without default image`() {
        // given
        val albumWithImage = DomainFixtures.getAlbum()
        val albumWithoutImage = DomainFixtures.getAlbum(images = listOf())
        val albums = listOf(albumWithImage, albumWithoutImage)
        coEvery { mockAlbumRepository.searchAlbum(any()) } returns albums

        // when
        val result = runBlocking { cut.execute() }

        // then
        result shouldBeEqualTo GetAlbumListUseCase.Result.Success(listOf(albumWithImage))
    }

    @Test
    fun `return error when repository throws an exception`() {
        // given
        val exception = UnknownHostException()
        coEvery { mockAlbumRepository.searchAlbum(any()) } throws exception

        // when
        val result = runBlocking { cut.execute() }

        // then
        result shouldBeEqualTo GetAlbumListUseCase.Result.Error(exception)
    }
}
