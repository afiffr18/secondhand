package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.NotificationDtoItem

class NotifikasiMapper : DomainMapper<NotificationDtoItem,Notifikasi> {

    override fun mapToDomainModel(modelDto: NotificationDtoItem): Notifikasi {
        return Notifikasi(
            id = modelDto.id,
            bidPrice = modelDto.bidPrice,
            status = modelDto.status,
            transactionDate = modelDto.transactionDate,
            updatedAt = modelDto.updatedAt
        )
    }

    fun toDomainList(initial : List<NotificationDtoItem>) : List<Notifikasi>{
        return initial.map {
            mapToDomainModel(it)
        }
    }
}