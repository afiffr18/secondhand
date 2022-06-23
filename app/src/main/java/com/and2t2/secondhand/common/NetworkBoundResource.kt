package com.and2t2.secondhand.common


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
            query().map{Resource.success(it)}
        }catch (throwable : Throwable){
            query().map {Resource.error(it, throwable.toString())}
        }
    }else{
        query().map{Resource.success(it)}
    }

    emitAll(flow)

}