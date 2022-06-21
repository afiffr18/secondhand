package com.and2t2.secondhand.common

interface DomainMapper<T,domainModel> {
    fun mapToDomainModel(modelDto:T) : domainModel
}