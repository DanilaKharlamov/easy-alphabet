package com.paragraph.ea.main.service

import com.paragraph.ea.main.common.AbstractTest
import com.paragraph.ea.main.common.CommonConstants.CONTENT_WAS_NOT_FOUND_BY_ID
import com.paragraph.ea.main.common.TestConstants.GE_LANG_UPLOAD_REQUEST
import com.paragraph.ea.main.common.TestConstants.OFFSET_UPLOAD_REQUEST
import com.paragraph.ea.main.common.TestConstants.RU_LANG_UPLOAD_REQUEST
import com.paragraph.ea.main.common.TestConstants.SUBJECT_TEXT_CONTENT_RESPONSE
import com.paragraph.ea.main.common.TestConstants.TEXT_UPLOAD_REQUEST
import com.paragraph.ea.main.exception.ContentNotFoundException
import com.paragraph.ea.main.exception.DictionaryNotFoundException
import com.paragraph.ea.main.repository.ContentRepository
import com.paragraph.ea.main.repository.entity.ContentEntity
import com.paragraph.ea.main.service.dto.ContentStatusEnum.*
import com.paragraph.ea.main.service.dto.TransformModeEnum.ALPHABET
import com.paragraph.ea.main.service.dto.TransformModeEnum.RANDOM
import com.paragraph.ea.main.service.dto.UploadContentRequest
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(provider = ZONKY, type = POSTGRES, refresh = AFTER_EACH_TEST_METHOD)
internal class ContentServiceTest(
    @Autowired
    private val contentRepository: ContentRepository,
    @Autowired
    private val contentService: ContentService
) : AbstractTest() {
    @BeforeEach
    fun init() {
        val contentEntityList = mutableListOf<ContentEntity>()
        for (i in 1..3) {
            contentEntityList.add(
                ContentEntity(
                    offSet = i,
                    subjectText = SUBJECT_FOR_MODERATION_CONTENT,
                    textLanguage = "ru",
                    transformMode = ALPHABET,
                    status = MODERATION
                )
            )
        }
        for (i in 1..3) {
            contentEntityList.add(
                ContentEntity(
                    offSet = i,
                    subjectText = SUBJECT_FOR_ACCEPTED_CONTENT,
                    textLanguage = "en",
                    transformMode = RANDOM,
                    status = REJECTED
                )
            )
        }
        for (i in 1..3) {
            contentEntityList.add(
                ContentEntity(
                    offSet = i,
                    subjectText = SUBJECT_FOR_REJECTED_CONTENT,
                    textLanguage = "ge",
                    transformMode = ALPHABET,
                    status = ACCEPTED
                )
            )
        }
        contentRepository.saveAll(contentEntityList)
    }

    @Nested
    inner class GetContentByStatus {
        @Test
        fun `when finding content with existing status and language expect list of ContentResponse`() {
            val moderationContentResponse = assertDoesNotThrow("Should not throw an exception") {
                contentService.getContentByStatus(MODERATION, "ru")
            }
            val acceptedContentResponse = assertDoesNotThrow("Should not throw an exception") {
                contentService.getContentByStatus(ACCEPTED, "ge")
            }
            val rejectedContentResponse = assertDoesNotThrow("Should not throw an exception") {
                contentService.getContentByStatus(REJECTED, "en")
            }

            assertNotNull(moderationContentResponse)
            moderationContentResponse.map {
                assertNotNull(it)
                assertEquals(it.mode, ALPHABET)
            }

            assertNotNull(acceptedContentResponse)
            acceptedContentResponse.map {
                assertNotNull(it)
                assertEquals(it.mode, ALPHABET)

            }

            assertNotNull(rejectedContentResponse)
            rejectedContentResponse.map {
                assertNotNull(it)
                assertEquals(it.mode, RANDOM)
            }
        }

        @Test
        fun `when finding content with language that doesn't exist expect empty list of ContentResponse`() {
            val contentResponse = assertDoesNotThrow(("Should not throw an exception")) {
                contentService.getContentByStatus(MODERATION, "test")
            }
            assertTrue(contentResponse.isEmpty())
        }
    }

    @Nested
    inner class GetContentById {
        @Test
        fun `when finding content with an existing ID expect ContentResponse`() {
            val contentResponseModeration = assertDoesNotThrow("Should not throw an exception") {
                contentService.getContentById(SUCCESSFUL_CONTENT_ID)
            }
            val contentResponseAccepted = assertDoesNotThrow("Should not throw an exception") {
                contentService.getContentById(4)
            }
            val contentResponseRejected = assertDoesNotThrow("Should not throw an exception") {
                contentService.getContentById(8)
            }

            assertNotNull(contentResponseModeration)
            assertEquals(contentResponseModeration.contentId, 1)
            assertEquals(contentResponseModeration.mode, ALPHABET)
            assertEquals(contentResponseModeration.subjectText, SUBJECT_FOR_MODERATION_CONTENT)

            assertNotNull(contentResponseAccepted)
            assertEquals(contentResponseAccepted.contentId, 4)
            assertEquals(contentResponseAccepted.mode, RANDOM)
            assertEquals(contentResponseAccepted.subjectText, SUBJECT_FOR_ACCEPTED_CONTENT)

            assertNotNull(contentResponseRejected)
            assertEquals(contentResponseRejected.contentId, 8)
            assertEquals(contentResponseRejected.mode, ALPHABET)
            assertEquals(contentResponseRejected.subjectText, SUBJECT_FOR_REJECTED_CONTENT)
        }

        @Test
        fun `when finding content that does not exist expect throw exception ContentNotFoundException`() {
            val exception = assertThrows(ContentNotFoundException::class.java) {
                contentService.getContentById(NEGATIVE_CONTENT_ID)
            }
            assertEquals(exception.localizedMessage, CONTENT_WAS_NOT_FOUND_BY_ID.plus(NEGATIVE_CONTENT_ID))
        }
    }

    @Nested
    inner class DeleteContent {
        @Test
        fun `when deleting content with an existing ID expect successful 204 no content response`() {
            assertDoesNotThrow("Should not throw an exception") {
                contentService.deleteContent(SUCCESSFUL_CONTENT_ID)
            }
        }

        @Test
        fun `when deleting content that does not exist expect throw exception ContentNotFoundException`() {
            val exception = assertThrows(ContentNotFoundException::class.java) {
                contentService.deleteContent(NEGATIVE_CONTENT_ID)
            }
            assertEquals(exception.localizedMessage, CONTENT_WAS_NOT_FOUND_BY_ID.plus(NEGATIVE_CONTENT_ID))
        }
    }

    @Nested
    inner class DeleteContentParagraphs {
        @Test
        fun `when deleting content paragraphs with an existing ID expect successful 204 no content response`() {
            assertDoesNotThrow("Should not throw an exception") {
                contentService.deleteContentParagraphs(SUCCESSFUL_CONTENT_ID)
            }
        }

        @Test
        fun `when deleting content paragraphs that does not exist expect throw exception ContentNotFoundException`() {
            val exception = assertThrows(ContentNotFoundException::class.java) {
                contentService.deleteContentParagraphs(NEGATIVE_CONTENT_ID)
            }
            assertEquals(exception.localizedMessage, CONTENT_WAS_NOT_FOUND_BY_ID.plus(NEGATIVE_CONTENT_ID))
        }
    }

    @Nested
    inner class AcceptContent {
        @Test
        fun `when changing status to ACCEPTED for existing ID expect successful 204 no content response`() {
            assertDoesNotThrow("Should not throw an exception") {
                contentService.acceptContent(SUCCESSFUL_CONTENT_ID)
            }
        }

        @Test
        fun `when changing status to ACCEPTED for not existing ID expect throw exception ContentNotFoundException`() {
            val exception = assertThrows(ContentNotFoundException::class.java) {
                contentService.acceptContent(NEGATIVE_CONTENT_ID)
            }
            assertEquals(exception.localizedMessage, CONTENT_WAS_NOT_FOUND_BY_ID.plus(NEGATIVE_CONTENT_ID))
        }
    }

    @Nested
    inner class RejectContent {
        @Test
        fun `when changing status to REJECTED for existing ID expect successful 204 no content response`() {
            assertDoesNotThrow("Should not throw an exception") {
                contentService.rejectContent(SUCCESSFUL_CONTENT_ID)
            }
        }

        @Test
        fun `when changing status to REJECTED for not existing ID expect throw exception ContentNotFoundException`() {
            val exception = assertThrows(ContentNotFoundException::class.java) {
                contentService.rejectContent(NEGATIVE_CONTENT_ID)
            }
            assertEquals(exception.localizedMessage, CONTENT_WAS_NOT_FOUND_BY_ID.plus(NEGATIVE_CONTENT_ID))
        }
    }

    @Nested
    inner class ChangeContentStatus {
        @Test
        fun `when changing status for existing ID expect successful 204 no content response`() {
            assertDoesNotThrow("Should not throw an exception") {
                contentService.changeContentStatus(SUCCESSFUL_CONTENT_ID, ACCEPTED)
                contentService.changeContentStatus(SUCCESSFUL_CONTENT_ID, REJECTED)
                contentService.changeContentStatus(SUCCESSFUL_CONTENT_ID, MODERATION)
            }
        }

        @Test
        fun `when changing status for not existing ID expect throw exception ContentNotFoundException`() {
            val exception = assertThrows(ContentNotFoundException::class.java) {
                contentService.rejectContent(NEGATIVE_CONTENT_ID)
            }
            assertEquals(exception.localizedMessage, CONTENT_WAS_NOT_FOUND_BY_ID.plus(NEGATIVE_CONTENT_ID))
        }
    }

    @Nested
    inner class GetTransformedContent {
        @Test
        fun `when uploading the correct user content expect ContentResponse`() {
            val request = UploadContentRequest(
                nativeLanguage = RU_LANG_UPLOAD_REQUEST,
                learningLanguage = GE_LANG_UPLOAD_REQUEST,
                offset = OFFSET_UPLOAD_REQUEST,
                mode = ALPHABET,
                text = TEXT_UPLOAD_REQUEST
            )

            val serviceResponse = assertDoesNotThrow {
                contentService.getTransformedContent(request)
            }

            assertEquals(serviceResponse.contentId, 10)
            assertEquals(serviceResponse.mode, ALPHABET)
            assertEquals(serviceResponse.subjectText, SUBJECT_TEXT_CONTENT_RESPONSE)

            assertEquals(serviceResponse.paragraphList[0].inOrder, 0)
            assertEquals(serviceResponse.paragraphList[0].nativeLetter, "А")
            assertEquals(serviceResponse.paragraphList[0].learningLetter, "ა")
            assertEquals(
                serviceResponse.paragraphList[0].textBody,
                "Пётр Гринёв родился в семье отстაвного офицерა აндрея Петровичა."
            )

            assertEquals(serviceResponse.paragraphList[1].inOrder, 1)
            assertEquals(serviceResponse.paragraphList[1].nativeLetter, "Б")
            assertEquals(serviceResponse.paragraphList[1].learningLetter, "ბ")
            assertEquals(
                serviceResponse.paragraphList[1].textBody,
                "აндрей Петрович Гринёв — отец Петрა, отстაвной офицер, строгий, прямой."
            )

            assertEquals(serviceResponse.paragraphList[2].inOrder, 2)
            assertEquals(serviceResponse.paragraphList[2].nativeLetter, "В")
            assertEquals(serviceResponse.paragraphList[2].learningLetter, "ვ")
            assertEquals(
                serviceResponse.paragraphList[2].textBody,
                "С пяти лет мაльчикა ვоспитыვაл и учил грაмоте стაрый слугა Сავельич."
            )

            assertEquals(serviceResponse.paragraphList[3].inOrder, 3)
            assertEquals(serviceResponse.paragraphList[3].nativeLetter, "Г")
            assertEquals(serviceResponse.paragraphList[3].learningLetter, "გ, ღ")
            assertTrue(
                serviceResponse.paragraphList[3].textBody.contains(
                    "აрхип Сავельич — стაрый слуგა გринёვა, сметлиვый, хитрый, ბережлиვый, предაнный."
                )
                        or serviceResponse.paragraphList[3].textBody.contains(
                    "აрхип Сავельич — стაрый слуღა ღринёვა, сметлиვый, хитрый, ბережлиვый, предაнный."
                )
            )
        }

        @Test
        fun `when user content is not correct expect throw exception`() {
            val request = UploadContentRequest(
                nativeLanguage = "TEesT1",
                learningLanguage = "TeEsT2",
                offset = OFFSET_UPLOAD_REQUEST,
                mode = ALPHABET,
                text = TEXT_UPLOAD_REQUEST
            )

            val exceptionResponse = assertThrows(DictionaryNotFoundException::class.java) {
                contentService.getTransformedContent(request)
            }
            assertEquals(exceptionResponse.localizedMessage, "These languages are not supported")
        }
    }

    companion object {
        private const val SUBJECT_FOR_MODERATION_CONTENT = "TEST-test_1213_test_MODERATION"
        private const val SUBJECT_FOR_ACCEPTED_CONTENT = "TEST-test_1213_test_ACCEPTED"
        private const val SUBJECT_FOR_REJECTED_CONTENT = "TEST-test_1213_test_REJECTED"

        private const val CONTENT_STATUS: String = "MODERATION"
        private const val SUCCESSFUL_CONTENT_ID: Long = 1
        private const val NEGATIVE_CONTENT_ID: Long = 99999
    }
}