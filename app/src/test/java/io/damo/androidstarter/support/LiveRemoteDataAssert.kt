package io.damo.androidstarter.support

import io.damo.androidstarter.support.RemoteData.Error
import io.damo.androidstarter.support.RemoteData.Loaded
import io.damo.androidstarter.support.RemoteData.Loading
import io.damo.androidstarter.support.RemoteData.NotLoaded
import org.assertj.core.api.Assertions.assertThat

class LiveRemoteDataAssert<T>(data: LiveRemoteData<T>) {

    private val value = data.value

    companion object {
        fun <T> assertThat(data: LiveRemoteData<T>) = LiveRemoteDataAssert(data)
    }

    fun isNotLoaded() = apply {
        assertThat(value).hasType<NotLoaded<T>>()
    }

    fun isLoading() = apply {
        assertThat(value).hasType<Loading<T>>()
    }

    fun isLoaded() = apply {
        assertThat(value).hasType<Loaded<T>>()
    }

    fun isLoadedWith(value: T) = apply {
        assertThat(value).isEqualTo(Loaded(value))
    }

    fun hasErrored() = apply {
        assertThat(value).hasType<Error<T>>()
    }

    fun hasErroredWith(reason: Explanation) = apply {
        assertThat(value).isEqualTo(Error<T>(reason))
    }
}
