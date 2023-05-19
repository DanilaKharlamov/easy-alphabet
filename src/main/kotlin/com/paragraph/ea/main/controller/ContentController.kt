package com.paragraph.ea.main.controller

import com.paragraph.ea.main.common.OpenApiConstants
import com.paragraph.ea.main.common.OpenApiConstants.DESCRIPTION_BAD_REQUEST
import com.paragraph.ea.main.common.OpenApiConstants.DESCRIPTION_OK
import com.paragraph.ea.main.service.dto.UploadContentRequest
import com.paragraph.ea.main.service.dto.ContentResponse
import com.paragraph.ea.main.service.dto.ContentStatusEnum
import com.paragraph.ea.main.service.dto.ErrorDto
import com.paragraph.ea.main.service.ContentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Contents", description = "Api для работы с контентом")
@RestController
@RequestMapping("/contents")
class ContentController(
    private var contentService: ContentService
) {

    @GetMapping
    @Operation(
        summary = "Получение списка контента по статусу",
        description = "Получение списка всего обработанного контента по выбранному статусу"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "400", description = DESCRIPTION_BAD_REQUEST,
                content = [Content(
                    schema = Schema(implementation = ErrorDto::class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE
                )]
            ),
            ApiResponse(
                responseCode = "200", description = DESCRIPTION_OK,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = ArraySchema(
                        schema = Schema(implementation = ContentResponse::class),
                        minItems = 0, maxItems = 100
                    )
                )]
            )
        ]
    )
    fun getAllContentByStatus(
        @Parameter(description = "Идентификатор статуса получаемой коллекции")
        @Schema(
            minLength = OpenApiConstants.MIN_STRING_LENGTH_1,
            maxLength = OpenApiConstants.MAX_STRING_LENGTH_255,
            required = true
        )
        @RequestParam status: String,

        @Parameter(description = "Идентификатор языка получаемой коллекции")
        @Schema(
            minLength = OpenApiConstants.MIN_STRING_LENGTH_1,
            maxLength = OpenApiConstants.MAX_STRING_LENGTH_255,
            required = false
        )
        @RequestParam language: String?

    ): List<ContentResponse> {
        return contentService.getContentByStatus(ContentStatusEnum.getStatus(status), language)
    }

    @GetMapping("/{contentId}")
    @Operation(
        summary = "Получение контента по id",
        description = "Получение одного элемента из списка контента по его id"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "400", description = DESCRIPTION_BAD_REQUEST,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ErrorDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = DESCRIPTION_OK,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ContentResponse::class)
                )]
            )
        ]
    )
    fun getContentById(
        @Parameter(description = "Уникальный идентификатор получаемого контента")
        @Schema(minimum = OpenApiConstants.ZERO_VALUE, maximum = OpenApiConstants.MAX_SAFE_LONG_VALUE)
        @PathVariable contentId: Long
    ): ContentResponse = contentService.getContentById(contentId)

    @PostMapping
    @Operation(summary = "Загрузка контента", description = "Загрузка пользовательского контента для обработки")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "400", description = DESCRIPTION_BAD_REQUEST,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ErrorDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = DESCRIPTION_OK,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ContentResponse::class)
                )]
            )
        ]
    )
    fun uploadContent(@RequestBody uploadContentRequest: UploadContentRequest): ContentResponse {
        return contentService.getTransformedContent(uploadContentRequest)
    }

    @DeleteMapping("/{contentId}")
    @Operation(
        summary = "Удаление контента полностью",
        description = "Удаление контента и всех ему принадлежащих абзацев"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "404", description = OpenApiConstants.DESCRIPTION_NOT_FOUND,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ErrorDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "204", description = OpenApiConstants.DESCRIPTION_NO_CONTENT,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE
                )]
            )
        ]
    )
    fun deleteContent(
        @Parameter(description = "Уникальный идентификатор удаляемого контента")
        @Schema(minimum = OpenApiConstants.ZERO_VALUE, maximum = OpenApiConstants.MAX_SAFE_LONG_VALUE)
        @PathVariable contentId: Long
    ): ResponseEntity<Void> {
        contentService.deleteContent(contentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{contentId}/paragraphs")
    @Operation(
        summary = "Удаление абзацев контента",
        description = "Удаление всех абзацев, принадлежащих контенту"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "404", description = OpenApiConstants.DESCRIPTION_NOT_FOUND,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ErrorDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "204", description = OpenApiConstants.DESCRIPTION_NO_CONTENT,
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE
                )]
            )
        ]
    )
    fun deleteContentParagraphs(
        @Parameter(description = "Уникальный идентификатор контента")
        @Schema(minimum = OpenApiConstants.ZERO_VALUE, maximum = OpenApiConstants.MAX_SAFE_LONG_VALUE)
        @PathVariable contentId: Long
    ): ResponseEntity<Void> {
        contentService.deleteContentParagraphs(contentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
