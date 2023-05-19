package com.paragraph.ea.main.service.dto

import com.paragraph.ea.main.common.OpenApiConstants
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Ответ с контентом", description = "Тело ответа с обработанным контентом")
data class ContentResponse(

    @Schema(
        title = "Id контента", type = "String",
        minimum = OpenApiConstants.MIN_STRING_LENGTH_1_VALUE, maximum = OpenApiConstants.MAX_SAFE_INT_VALUE,
        pattern = OpenApiConstants.DIGITS_ONLY_PATTERN, example = "12"
    )
    var contentId: Long,

    @Schema(
        title = "Режим трансформации текста", type = "String",
        nullable = true, maxLength = OpenApiConstants.MAX_MODE_LENGTH,
        pattern = OpenApiConstants.ALPHANUMERIC_PATTERN, example = "ALPHABET"
    )
    var mode: TransformModeEnum,

    @Schema(
        title = "Первый абзац текста в исходном виде", type = "String",
        minLength = OpenApiConstants.MIN_STRING_LENGTH_1, maxLength = OpenApiConstants.MAX_STRING_LENGTH_255,
        pattern = OpenApiConstants.ANY_SYMBOL_PATTERN
    )
    var subjectText: String,

    @ArraySchema(
        arraySchema = Schema(
            description = "Список элементов(параграфов) обработанного контента",
            implementation = ParagraphDto::class
        ),
        minItems = OpenApiConstants.MIN_STRING_LENGTH_1,
        maxItems = OpenApiConstants.MAX_SAFE_INT
    )
    var paragraphList: MutableList<ParagraphDto>
)
