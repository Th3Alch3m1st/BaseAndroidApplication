package com.learn.codingchallenge.network.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created By Rafiqul Hasan
 */

@RunWith(JUnit4::class)
class GIFResponseTest {
    companion object {
        //pagination
        private const val OFFSET = 0
        private const val TOTAl_COUNT = 1000
        private const val COUNT = 10

        //data
        private const val ID = "SggILpMXO7Xt6"
        private const val RATING = "g"
        private const val SOURCE = "https://www.youtube.com/watch?v=eVw6i4l6xTg"
        private const val TITLE = "Funny Face Dog GIF"

        //images
        private const val IMAGE_ORIGINAL_URL =
            "https://media1.giphy.com/media/SggILpMXO7Xt6/giphy.gif?cid=d5fd0016mhf401cgzhmqdymqs8ts7lyc1v5cmfy7arw71k42&rid=giphy.gif&ct=g"
        private const val IMAGE_DOWNSIZED_URL =
            "https://media1.giphy.com/media/SggILpMXO7Xt6/giphy.gif?cid=d5fd0016mhf401cgzhmqdymqs8ts7lyc1v5cmfy7arw71k42&rid=giphy.gif&ct=g"
    }

    private lateinit var gifResponse: GIFResponse


    @Before
    fun setUp() {
        val pagination = Pagination(
            offset = OFFSET,
            totalCount = TOTAl_COUNT,
            count = COUNT
        )

        val imageOriginal = Image(
            url = IMAGE_ORIGINAL_URL
        )
        val imageDownSize = Image(
            url = IMAGE_DOWNSIZED_URL
        )
        val images = Images(
            original = imageOriginal,
            downsized = imageDownSize
        )

        val dataItem = GifInfo(
            id = ID,
            images = images,
            rating = RATING,
            source = SOURCE,
            title = TITLE
        )

        gifResponse = GIFResponse(
            pagination = pagination,
            gifList = listOf(dataItem)
        )
    }

    @Test
    fun testGifResponseObject() {
        //pagination object
        assertThat(gifResponse.pagination?.offset).isEqualTo(OFFSET)
        assertThat(gifResponse.pagination?.count).isEqualTo(COUNT)
        assertThat(gifResponse.pagination?.totalCount).isEqualTo(TOTAl_COUNT)

        //data
        assertThat(gifResponse.gifList?.get(0)?.id).isEqualTo(ID)
        assertThat(gifResponse.gifList?.get(0)?.rating).isEqualTo(RATING)
        assertThat(gifResponse.gifList?.get(0)?.source).isEqualTo(SOURCE)
        assertThat(gifResponse.gifList?.get(0)?.title).isEqualTo(TITLE)
        assertThat(gifResponse.gifList?.get(0)?.images?.original?.url).isEqualTo(IMAGE_ORIGINAL_URL)
        assertThat(gifResponse.gifList?.get(0)?.images?.downsized?.url).isEqualTo(
            IMAGE_DOWNSIZED_URL
        )
    }
}