package com.igorwojda.showcase.feature.album.data.network.model

import com.igorwojda.showcase.feature.album.data.DataFixtures
import com.igorwojda.showcase.feature.album.data.network.enum.AlbumImageSizeJson
import com.igorwojda.showcase.feature.album.domain.enum.AlbumDomainImageSize
import com.igorwojda.showcase.feature.album.domain.model.Album
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class AlbumJsonDataModelTest {

    @Test
    fun `data model with full data maps to AlbumDomainModel`() {
        // given
        val cut = DataFixtures.getAlbum()

        // when
        val domainModel = cut.toDomainModel()

        // then
        domainModel shouldBeEqualTo Album(
            cut.name,
            cut.artist,
            cut.images?.map { it.toDomainModel() } ?: listOf(),
            cut.wiki?.toDomainModel(),
            cut.mbId
        )
    }

    @Test
    fun `data model with missing data maps to AlbumDomain§Model`() {
        // given
        val cut = DataFixtures.getMinimalAlbum()

        // when
        val domainModel = cut.toDomainModel()

        // then
        domainModel shouldBeEqualTo Album(
            name = "name", artist = "artist", images = emptyList(), wiki = null, mbId = "mbId"
        )
    }

    @Test
    fun `mapping filters out unknown size`() {
        // given
        val albumDataImages = listOf(AlbumImageSizeJson.EXTRA_LARGE, AlbumImageSizeJson.UNKNOWN)
            .map { DataFixtures.getAlbumImage(size = it) }
        val cut = DataFixtures.getAlbum(images = albumDataImages)

        // when
        val domainModel = cut.toDomainModel()

        // then
        domainModel.images.single { it.size == AlbumDomainImageSize.EXTRA_LARGE }
    }

    @Test
    fun `mapping filters out blank url`() {
        // given
        val albumDataImages = listOf("", "url")
            .map { DataFixtures.getAlbumImage(url = it) }

        val cut = DataFixtures.getAlbum(images = albumDataImages)

        // when
        val domainModel = cut.toDomainModel()

        // then
        domainModel.images.single { it.url == "url" }
    }
}
