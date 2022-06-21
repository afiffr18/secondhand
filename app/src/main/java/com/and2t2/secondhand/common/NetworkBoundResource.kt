package com.and2t2.secondhand.common

import id.afif.binarchallenge8.domain.util.Resource
import kotlinx.coroutines.flow.*

inline fun<ResultType,RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if(shouldFetch(data)){
        emit(Resource.loading(data))
        try {
            saveFetchResult(fetch())
            query().map{Resource.succes(it)}
        }catch (throwable : Throwable){
            query().map {Resource.error(it, throwable.toString())}
        }
    }else{
        query().map{Resource.succes(it)}
    }

    emitAll(flow)

}