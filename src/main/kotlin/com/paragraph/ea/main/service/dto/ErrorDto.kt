package com.paragraph.ea.main.service.dto

import com.paragraph.ea.main.common.OpenApiConstants
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Ошибка", description = "Информация о возникшей ошибке во время запроса")
data class ErrorDto(
    @Schema(
        title = "Краткое описание ошибки", required = true, minLength = 1, maxLength = 200,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN, example = "Ошибка формата запроса"
    )
    val title: String?,

    @Schema(
        title = "Описание ошибки", required = true, minLength = 1, maxLength = 255,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN, example = "Не удалось разобрать тело запроса"
    )
    val detail: String?,

    @Schema(
        title = "Уникальный код ошибки. В разрезе системы, сервиса и кода: ea.{наименование-сервиса}.{код-ошибки}.{код-ошибки}",
        required = true, minLength = 1, maxLength = 100,
        pattern = OpenApiConstants.ERROR_CODE_PATTERN, example = "easy-alphabet.common-error"
    )
    val code: String?
)