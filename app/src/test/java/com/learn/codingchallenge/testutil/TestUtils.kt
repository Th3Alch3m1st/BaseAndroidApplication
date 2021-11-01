package com.learn.codingchallenge.testutil

import com.learn.codingchallenge.network.model.GIFResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.IOException

/**
 * Created By Rafiqul Hasan
 */
object TestUtils {
    @Throws(IOException::class)
    private fun readFileToString(contextClass: Class<*>, fileName: String): String {
        contextClass.getResourceAsStream(fileName)
            ?.bufferedReader().use {
                val jsonString = it?.readText() ?: ""
                it?.close()
                return jsonString
            }
    }

    fun getTestData(fileName: String): GIFResponse {
        val moshi = Moshi.Builder()
            .build()
        val jsonAdapter: JsonAdapter<GIFResponse> = moshi.adapter(GIFResponse::class.java)
        val jsonString = readFileToString(TestUtils::class.java, "/$fileName")
        return jsonAdapter.fromJson(jsonString)!!
    }
}