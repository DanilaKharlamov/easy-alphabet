package com.paragraph.ea.main.service.dto

import com.paragraph.ea.main.exception.StatusNotFoundException

enum class ContentStatusEnum(val value: Int) {

    ACCEPTED(1),
    REJECTED(2),
    MODERATION(3);

    companion object {
        fun getStatus(requestStatus: String): ContentStatusEnum {
            return ContentStatusEnum.values().find { it.name.equals(requestStatus, ignoreCase = true) }
                ?: throw StatusNotFoundException(
                    "Current status does not exist. Please use one of these options: ${
                        ContentStatusEnum.values().joinToString { it.name }
                    }"
                )
        }
    }
}
