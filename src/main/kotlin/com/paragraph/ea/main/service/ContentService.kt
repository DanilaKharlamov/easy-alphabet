package com.paragraph.ea.main.service

import com.paragraph.ea.main.common.CommonConstants.CONTENT_WAS_NOT_FOUND_BY_ID
import com.paragraph.ea.main.service.dto.TransformModeEnum.ALPHABET
import com.paragraph.ea.main.service.dto.TransformModeEnum.RANDOM
import com.paragraph.ea.main.repository.entity.ContentEntity
import com.paragraph.ea.main.repository.entity.ParagraphEntity
import com.paragraph.ea.main.exception.ContentNotFoundException
import com.paragraph.ea.main.repository.ContentRepository
import com.paragraph.ea.main.repository.LetterRepository
import com.paragraph.ea.main.repository.ParagraphRepository
import com.paragraph.ea.main.service.dto.*
import com.paragraph.ea.main.service.mapper.ContentMapper
import com.paragraph.ea.main.util.StringUtil
import com.paragraph.ea.main.util.StringUtil.replaceMultiple
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val paragraphRepository: ParagraphRepository,
    private val letterRepository: LetterRepository,

    @Value("\${ea.content.subject-size}")
    private val subjectSize: Int,
    @Value("\${ea.content.offset-percentage}")
    private val offsetPercentage: Double
) {
    private val log = KotlinLogging.logger {}

    @Transactional
    fun getContentByStatus(contentStatus: ContentStatusEnum, language: String?): List<ContentResponse> {
        val contentEntityList: List<ContentEntity> = if (language.isNullOrEmpty()) {
            contentRepository.getByStatus(contentStatus)
        } else {
            contentRepository.getByStatusAndTextLanguage(contentStatus, language)
        }
        return contentEntityList.map { ContentMapper.mapToContentDto(it) }
    }

    @Transactional
    fun getContentById(contentId: Long): ContentResponse =
        contentRepository.findById(contentId)
            .map(ContentMapper::mapToContentDto)
            .orElseThrow { ContentNotFoundException(CONTENT_WAS_NOT_FOUND_BY_ID.plus(contentId)) }
            .also { log.debug("Content with id {} was successfully received", contentId) }

    fun deleteContent(contentId: Long) {
        val content = contentRepository.findByIdOrNull(contentId)
            ?: throw ContentNotFoundException(CONTENT_WAS_NOT_FOUND_BY_ID.plus(contentId))
        contentRepository.delete(content)
        log.debug("Content with id {} has been successfully deleted", contentId)
    }

    fun deleteContentParagraphs(contentId: Long) {
        val content = contentRepository.findByIdOrNull(contentId)
            ?: throw ContentNotFoundException(CONTENT_WAS_NOT_FOUND_BY_ID.plus(contentId))
        val contentParagraphs = paragraphRepository.findAllByContent(content)
        paragraphRepository.deleteAll(contentParagraphs)
        log.debug("All paragraphs from content with id {} have been successfully removed", contentId)
    }

    fun acceptContent(contentId: Long) {
        updateContentStatus(contentId, ContentStatusEnum.ACCEPTED)
    }

    fun rejectContent(contentId: Long) {
        updateContentStatus(contentId, ContentStatusEnum.REJECTED)
    }

    fun changeContentStatus(contentId: Long, status: ContentStatusEnum) {
        updateContentStatus(contentId, status)
    }

    private fun updateContentStatus(contentId: Long, status: ContentStatusEnum) {
        val content = contentRepository.findByIdOrNull(contentId)
            ?: throw ContentNotFoundException(CONTENT_WAS_NOT_FOUND_BY_ID.plus(contentId))
        content.status = status
        content.updatedAt = LocalDateTime.now()
        contentRepository.save(content)
    }

    @Transactional
    fun getTransformedContent(contentRequest: UploadContentRequest): ContentResponse {
        log.debug("Transforming content with the following parameters: {}", contentRequest)
        val contentEntity = createContentEntity(contentRequest)
        val dictionary = letterRepository.getDictionary(contentRequest.nativeLanguage, contentRequest.learningLanguage)
        contentEntity.paragraphEntityList = getTransformedParagraphs(contentRequest.text, dictionary, contentEntity)
        contentRepository.save(contentEntity)
        return ContentMapper.mapToContentDto(contentEntity)
    }

    private fun createContentEntity(uploadContentRequest: UploadContentRequest) =
        ContentEntity(
            offSet = uploadContentRequest.offset ?: (uploadContentRequest.text.length * offsetPercentage).toInt(),
            subjectText = uploadContentRequest.text.take(subjectSize).trim(),
            textLanguage = uploadContentRequest.nativeLanguage,
            transformMode = uploadContentRequest.mode ?: ALPHABET
        )

    private fun getTransformedParagraphs(
        text: String,
        dictionary: List<DictionaryDto>,
        contentEntity: ContentEntity
    ): MutableSet<ParagraphEntity> {
        val paragraphEntitySet: MutableSet<ParagraphEntity> = mutableSetOf()
        val paragraphList: MutableList<String> = textIntoParagraphs(text, dictionary.size, contentEntity.offSet)

        var inOrder = 0
        paragraphList.forEach { paragraphText ->
            val usedLetterPairs = getUsedAndNextLetterPairs(dictionary, contentEntity.transformMode)

            val transformedParagraph = usedLetterPairs.fold(paragraphText) { paragraph, letterPair ->
                paragraph.replaceMultiple(letterPair.nativeLetter, letterPair.learningLetter)
            }

            updateDictionaryUsage(dictionary, usedLetterPairs)

            ParagraphEntity(
                StringUtil.validateParagraph(transformedParagraph),
                usedLetterPairs.last().nativeLetter.joinToString(),
                usedLetterPairs.last().learningLetter.joinToString(),
                inOrder,
                contentEntity
            ).apply {
                paragraphEntitySet.add(this)
                inOrder++
            }
        }
        return paragraphEntitySet
    }

    private fun textIntoParagraphs(text: String, dictionarySize: Int, offset: Int): MutableList<String> {
        val paragraphList = mutableListOf<String>()
        var paragraph: String
        var order = 0
        var lastIndex = 0
        var nextIndex: Int

        text.forEachIndexed { globalIndex, _ ->
            if (globalIndex % offset == 0 && globalIndex != 0 && globalIndex > lastIndex + (offset / 2)) {
                if (order >= dictionarySize) return@forEachIndexed

                nextIndex = findNearestEndpoint(text, lastIndex, globalIndex)

                paragraph = if (order < dictionarySize - 1) {
                    text.substring(lastIndex).take(nextIndex - lastIndex)
                } else {
                    text.substring(lastIndex)
                }

                paragraphList.add(paragraph)
                lastIndex = nextIndex
                order++
            }
        }
        return paragraphList
    }

    private fun findNearestEndpoint(text: String, lastEndpoint: Int, globalIndex: Int): Int {
        val forwardIndex = StringUtil.indexOfNearestEndpoint(text.substring(globalIndex), text, globalIndex)
        val backwardIndex = StringUtil.indexOfNearestEndpoint(
            text.substring(lastEndpoint, globalIndex).reversed(),
            text,
            globalIndex
        )
        return when {
            forwardIndex == -1 -> text.lastIndex + 1
            forwardIndex < backwardIndex && backwardIndex >= 0 || backwardIndex == -1 -> globalIndex + forwardIndex + 2
            else -> globalIndex - backwardIndex + 1
        }
    }

    private fun getUsedAndNextLetterPairs(
        dictionary: List<DictionaryDto>,
        mode: TransformModeEnum
    ): List<DictionaryDto> {
        val nextLetterPair = getNextLetterPair(dictionary, mode)
        return dictionary.filter { it.isUsed } + nextLetterPair
    }

    private fun getNextLetterPair(dictionary: List<DictionaryDto>, mode: TransformModeEnum): DictionaryDto {
        return if (mode == RANDOM) {
            dictionary.filter { !it.isUsed }.random()
        } else {
            dictionary.first { !it.isUsed }
        }
    }

    private fun updateDictionaryUsage(dictionary: List<DictionaryDto>, letterPairs: List<DictionaryDto>) {
        val lastLetterPair = letterPairs.last()
        dictionary.filter { it.learningLetter == lastLetterPair.learningLetter }
            .forEach { it.isUsed = true }
    }
}
