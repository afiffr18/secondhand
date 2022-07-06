package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.auth.AuthUserDtoItem

class AuthUserMapper: DomainMapper<AuthUserDtoItem, AuthUser> {
    override fun mapToDomainModel(modelDto: AuthUserDtoItem): AuthUser {
        return AuthUser(
            id = modelDto.id,
            fullName = modelDto.fullName,
            city = modelDto.city,
            address = modelDto.address,
            phoneNumber = modelDto.phoneNumber,
            imageUrl = modelDto.imageUrl
        )
    }

    fun toDomainList(initial : List<AuthUserDtoItem>) : List<AuthUser>{
        return initial.map {
            mapToDomainModel(it)
        }
    }
}