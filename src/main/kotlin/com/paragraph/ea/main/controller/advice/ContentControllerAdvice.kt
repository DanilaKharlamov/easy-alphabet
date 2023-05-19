package com.paragraph.ea.main.controller.advice

import com.paragraph.ea.main.service.dto.ErrorDto
import com.paragraph.ea.main.exception.ContentNotFoundException
import com.paragraph.ea.main.exception.DictionaryNotFoundException
import com.paragraph.ea.main.exception.StatusNotFoundException
import com.paragraph.ea.main.util.ErrorUtil
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils.isEmpty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ContentControllerAdvice {

    @Value("COMMON_SERVICE_EXCEPTION")
    private var badRequestMessage: String = "Something going wrong, unexpected exception"

    @Value("CONTENT_NOT_FOUND_EXCEPTION")
    private var contentNotFoundException: String = "Exception while trying to find content."

    @Value("STATUS_NOT_FOUND_EXCEPTION")
    private var statusNotFoundException: String = "Exception while trying to find content by not existed status."

    @Value("DICTIONARY_NOT_FOUND_EXCEPTION")
    private var dictionaryNotFoundException: String = "Exception while trying to get dictionary by not supported languages."

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ContentNotFoundException::class)
    fun handleContentNotFoundException(exception: ContentNotFoundException): ResponseEntity<ErrorDto> {
        val detailMessage =
            if (!isEmpty(exception.localizedMessage)) exception.localizedMessage else contentNotFoundException

        logger.warn("Exception while trying search content. ${exception.localizedMessage}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorUtil.buildError(
                ErrorUtil.NOT_FOUND_MESSAGE,
                detailMessage,
                "easy-alphabet-main",
                ErrorUtil.EA_CODE
            )
        )
    }

    @ExceptionHandler(StatusNotFoundException::class)
    fun handleStatusNotFoundException(exception: StatusNotFoundException): ResponseEntity<ErrorDto> {
        val detailMessage =
            if (!isEmpty(exception.localizedMessage)) exception.localizedMessage else statusNotFoundException

        logger.warn("Exception while trying to get content list. Message:${exception.localizedMessage}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorUtil.buildError(
                ErrorUtil.NOT_FOUND_MESSAGE,
                detailMessage,
                "easy-alphabet-main",
                ErrorUtil.EA_CODE
            )
        )
    }

    @ExceptionHandler(DictionaryNotFoundException::class)
    fun handleDictionaryNotFoundException(exception: Exception): ResponseEntity<ErrorDto>{
        val detailMessage =
            if (!isEmpty(exception.localizedMessage)) exception.localizedMessage else dictionaryNotFoundException

        logger.warn("Exception while trying to get dictionary by not supported languages. Message:${exception.localizedMessage}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorUtil.buildError(
                ErrorUtil.NOT_FOUND_MESSAGE,
                detailMessage,
                "easy-alphabet-main",
                ErrorUtil.EA_CODE
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(exception: Exception): ResponseEntity<ErrorDto> {
        val detailMessage = if (!isEmpty(exception.localizedMessage)) exception.localizedMessage else badRequestMessage
        logger.error("An unexpected error occurred! Message:{}", exception.localizedMessage, exception)

        return ResponseEntity.badRequest().body(
            ErrorUtil.buildError(
                ErrorUtil.COMMON_ERROR_TITLE,
                detailMessage,
                "easy-alphabet-main",
                ErrorUtil.COMMON_ERROR_CODE
            )
        )
    }
}
