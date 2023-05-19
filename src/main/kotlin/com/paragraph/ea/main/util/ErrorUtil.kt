package com.paragraph.ea.main.util

import com.paragraph.ea.main.service.dto.ErrorDto
import java.util.*

class ErrorUtil {

    init {
        throw IllegalStateException("Utility class")
    }

    companion object {
        val NOT_FOUND_MESSAGE = "Ошибка поиска"
        val COMMON_ERROR_TITLE = "Общая ошибка"
        val COMMON_ERROR_CODE = "common-error"
        val EA_CODE = "ea"

        fun buildError(
            title: String,
            detailMessage: String,
            serviceName: String,
            vararg detailCodes: String
        ): ErrorDto {
            return ErrorDto(
                title = title,
                code = constructErrorDtoCode(serviceName, *detailCodes),
                detail = detailMessage
                )
        }

        private fun constructErrorDtoCode(serviceName: String, vararg detailCodes: String): String {
            val joiner = StringJoiner(".")
            joiner.add(EA_CODE).add(serviceName)
            val details = detailCodes
                .map { detailCode: String -> detailCode.replace(" ", "").lowercase() }
                .joinToString { "." }
            if (details.isNotBlank()) {
                joiner.add(details)
            }
            return joiner.toString()
        }
    }
}