package com.paragraph.ea.main.service.dto

import com.paragraph.ea.main.common.OpenApiConstants
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Тело абзаца", description = "Тело одного обработанного обзаца")
data class ParagraphDto(

    @Schema(
        title = "Буква в абзаце, которая была изменена",
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MIN_STRING_LENGTH_1,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN, example = "А"
    )
    var nativeLetter: String,

    @Schema(
        title = "Буква из изучаемого алфавита, на которую происходит замена в абзаце",
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MIN_STRING_LENGTH_1,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN, example = "ა"
    )
    var learningLetter: String,

    @Schema(
        title = "Текст обработанного абзаца",
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MAX_SAFE_INT,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN, example = "Стрингა ст"
    )
    var textBody: String,

    @Schema(
        title = "Порядковый номер абзаца в тексте",
        minimum = OpenApiConstants.MIN_STRING_LENGTH_1_VALUE, maximum = OpenApiConstants.MAX_SAFE_INT_VALUE,
        pattern = OpenApiConstants.DIGITS_ONLY_PATTERN, example = "0"
    )
    var inOrder: Int
)