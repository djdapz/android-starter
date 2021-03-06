package io.damo.androidstarter.backend

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.damo.androidstarter.support.Failure
import io.damo.androidstarter.support.Result
import io.damo.androidstarter.support.Success
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.readValue

fun requestBuilder(url: String): Request.Builder =
    Request.Builder().url(url)

fun <K, V> Request.Builder.postJson(vararg pair: Pair<K, V>): Request.Builder =
    post(jsonBodyOf(*pair))

fun <K, V> Request.Builder.putJson(vararg pair: Pair<K, V>): Request.Builder =
    put(jsonBodyOf(*pair))

fun jsonBody(body: Any): RequestBody =
    objectMapper
        .writeValueAsString(body)
        .toRequestBody(jsonMediaType)

fun <K, V> jsonBodyOf(vararg pair: Pair<K, V>): RequestBody =
    jsonBody(mapOf(*pair))

fun Request.Builder.execute(): Result<Response> =
    execute(okHttp)

fun Request.Builder.execute(okHttpClient: OkHttpClient): Result<Response> =
    try {
        val response = okHttpClient.newCall(build()).execute()
        Success(response)
    } catch (e: IOException) {
        Failure("There was an error contacting the server: ${e.message}", e)
    }

fun Result<Response>.requireStatusCode(expectedCode: Int): Result<Response> =
    bind {
        when (it.code) {
            expectedCode -> Success(it)
            else -> Failure<Response>("Expected server response to be a $expectedCode")
        }
    }

fun Response.closeQuietly(): Unit =
    use { Unit }

inline fun <reified T : Any?> Response.parseJson(): Result<T> =
    try {
        use {
            body
                ?.let { Success(it.parseDangerously<T>()) }
                ?: Failure("Could not parse null response body")
        }
    } catch (e: IOException) {
        Failure("There was an error parsing the JSON response: ${e.message}", e)
    }

inline fun <reified T : Any?> ResponseBody.parseDangerously(): T =
    objectMapper.readValue(byteStream())

val okHttp =
    OkHttpClient()

val objectMapper: ObjectMapper =
    jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun Any.asJson() = objectMapper.writeValueAsString(this)

inline fun <reified T> String.fromJson(): T = objectMapper.readValue(this)

val jsonMediaType: MediaType =
    "application/json".toMediaType()
