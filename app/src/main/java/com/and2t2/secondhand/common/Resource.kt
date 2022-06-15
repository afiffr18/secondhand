package id.afif.binarchallenge8.domain.util

import id.afif.binarchallenge7.Model.Status

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> succes(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)
    }
}