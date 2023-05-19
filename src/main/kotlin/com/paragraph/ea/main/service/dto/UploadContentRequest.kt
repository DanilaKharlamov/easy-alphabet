package com.paragraph.ea.main.service.dto

import com.paragraph.ea.main.common.OpenApiConstants
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Schema(title = "Загрузка контента", description = "Тело запроса для загрузки и трансформации текста")
@Serializable
data class UploadContentRequest(

    @Schema(
        title = "Код родного языкового алфавита",
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MAX_STRING_LENGTH_255,
        pattern = OpenApiConstants.LETTERS_ONLY_ENG_PATTERN, example = "ru", required = true
    )
    val nativeLanguage: String,

    @Schema(
        title = "Код изучаемого языкового алфавита",
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MAX_STRING_LENGTH_255,
        pattern = OpenApiConstants.LETTERS_ONLY_ENG_PATTERN, example = "ge", required = true
    )
    val learningLanguage: String,

    @Schema(
        title = "Диапозон для изменения буквы", required = false,
        minimum = OpenApiConstants.ZERO_VALUE, maximum = OpenApiConstants.MAX_SAFE_INT_VALUE,
        pattern = OpenApiConstants.DIGITS_ONLY_PATTERN, example = "10"
    )
    val offset: Int? = null,
    @Schema(
        title = "Режим трансформации текста", required = false, type = "String",
        minLength = OpenApiConstants.ZERO, maxLength = OpenApiConstants.MAX_MODE_LENGTH,
        pattern = OpenApiConstants.ALPHANUMERIC_PATTERN, example = "ALPHABET"
    )
    val mode: TransformModeEnum? = null,

    @Schema(
        title = "Текст", required = true,
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MAX_SAFE_INT,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN, example = "Test text, something with any symbols 123$%_1*&^%$"
    )
    val text: String

) {

    override fun toString(): String {
        return "nativeLanguage: ${nativeLanguage}, " +
                "learningLanguage: ${learningLanguage}, " +
                "offset: ${offset}, " +
                "mode: ${mode}"
    }
}