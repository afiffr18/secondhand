package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.common.toFormatDate
import com.and2t2.secondhand.data.remote.dto.notification.NotificationDtoItem

class NotifikasiMapper : DomainMapper<NotificationDtoItem,Notifikasi> {

    override fun mapToDomainModel(modelDto: NotificationDtoItem): Notifikasi {
        return Notifikasi(
            id = modelDto.id,
            bidPrice = modelDto.bidPrice,
            productId = modelDto.productId,
            status = modelDto.status,
            transactionDate = modelDto.transactionDate,
            updatedAt = modelDto.updatedAt.toFormatDate(),
            read = modelDto.read,
            imageUrl = modelDto.imageUrl

        )
    }

    fun toDomainList(initial : List<NotificationDtoItem>) : List<Notifikasi>{
        return initial.map {
            mapToDomainModel(it)
        }
    }
}