package com.paragraph.ea.main.controller

import com.cedarsoftware.util.StringUtilities.getRandomString
import com.paragraph.ea.main.common.AbstractTest
import com.paragraph.ea.main.common.TestConstants.SUBJECT_TEXT_CONTENT_RESPONSE
import com.paragraph.ea.main.common.TestConstants.TEXT_UPLOAD_REQUEST
import com.paragraph.ea.main.service.ContentService
import com.paragraph.ea.main.service.dto.*
import com.paragraph.ea.main.service.dto.TransformModeEnum.ALPHABET
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@WebMvcTest(ContentController::class)
internal class ContentControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) : AbstractTest() {
    @MockkBean
    private lateinit var contentService: ContentService

    @Nested
    inner class GetAllContentByStatus {
        @Test
        fun `when find content by status without language expect list of ContentResponse`() {
            val response = mutableListOf<ContentResponse>()
            every {
                contentService.getContentByStatus(
                    ContentStatusEnum.getStatus(CONTENT_STATUS),
                    null
                )
            } returns response

            mockMvc.get("/contents?status={contentStatus}", CONTENT_STATUS)
                .andExpect { status { isOk() } }
                .andExpect {
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        emptyList<ContentResponse>()
                    }
                }

            verify(exactly = 1) { contentService.getContentByStatus(ContentStatusEnum.getStatus(CONTENT_STATUS), null) }
        }

        @Test
        fun `when find content by status with language expect list of ContentResponse`() {
            val response = mutableListOf<ContentResponse>()
            every {
                contentService.getContentByStatus(
                    ContentStatusEnum.getStatus(CONTENT_STATUS),
                    "ru"
                )
            } returns response

            mockMvc.get("/contents?status={contentStatus}&language={language}", CONTENT_STATUS, "ru")
                .andExpect { status { isOk() } }
                .andExpect {
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        emptyList<ContentResponse>()
                    }
                }

            verify(exactly = 1) {
                contentService.getContentByStatus(
                    ContentStatusEnum.getStatus(Companion.CONTENT_STATUS),
                    "ru"
                )
            }
        }

        @Test
        fun `when find content by undefined status expect exception`() {
            mockMvc.get("/contents?status={contentStatus}&language={language}", "TEST-TEST", "ru")
                .andExpect { status { NOT_FOUND } }
                .andExpect {
                    jsonPath("\$.title") { value("Ошибка поиска") }
                }
        }
    }

    @Nested
    inner class GetContentById {
        @Test
        fun `when find content by id expect ContentResponse`() {
            val response = ContentResponse(
                Companion.CONTENT_ID,
                ALPHABET,
                getRandomString(Random(), 150, 250),
                mutableListOf()
            )
            every { contentService.getContentById(Companion.CONTENT_ID) } returns response

            mockMvc.get("/contents/{contentId}", Companion.CONTENT_ID)
                .andExpect { status { isOk() } }
                .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
                .andExpect {
                    jsonPath("\$.contentId") { value("1") }
                    jsonPath("\$.mode") { value("ALPHABET") }
                    jsonPath("\$.subjectText") { value(response.subjectText) }
                    jsonPath("\$.paragraphList") { emptyList<ParagraphDto>() }
                }

            verify(exactly = 1) { contentService.getContentById(Companion.CONTENT_ID) }
        }
    }

    @Nested
    inner class UploadContent {
        @Test
        fun `when upload user-generated content expect ContentResponse`() {
            val paragraph0 = ParagraphDto(
                "А",
                "ა",
                "Пётр Гринёв родился в семье отстაвного офицерა აндрея Петровичა.",
                0
            )
            val paragraph1 = ParagraphDto(
                "Б",
                "ბ",
                "აндрей Петрович Гринёв — отец Петрა, отстაвной офицер, строгий, прямой.",
                1
            )
            val paragraph2 = ParagraphDto(
                "В",
                "ვ",
                "С пяти лет мაльчикა ვоспитыვაл и учил грაмоте стაрый слугა Сავельич.",
                2
            )
            val paragraph3 = ParagraphDto(
                "Г",
                "გ, ღ",
                "აрхип Сავельич — стაрый слуგა გринёვა, сметлиვый, хитрый, ბережлиვый, предაнный.",
                3
            )
            val response = ContentResponse(
                contentId = 10,
                mode = ALPHABET,
                subjectText = SUBJECT_TEXT_CONTENT_RESPONSE,
                paragraphList = mutableListOf(paragraph0, paragraph1, paragraph2, paragraph3)
            )
            val request = UploadContentRequest(
                nativeLanguage = "ru",
                learningLanguage = "ge",
                offset = 200,
                mode = ALPHABET,
                text = TEXT_UPLOAD_REQUEST
            )
            every { contentService.getTransformedContent(request) } returns response


            mockMvc.post("/contents") {
                content = objectMapper.writeValueAsString(request)
                contentType = MediaType.APPLICATION_JSON
            }
                .andExpect { status { isOk() } }
                .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
                .andExpect {
                    jsonPath("\$.contentId") { value(10) }
                    jsonPath("\$.mode") { value("ALPHABET") }
                    jsonPath("\$.subjectText") { value(SUBJECT_TEXT_CONTENT_RESPONSE) }
                    jsonPath("\$.paragraphList.size()") { value(4) }
                }

            verify(exactly = 1) { contentService.getTransformedContent(request) }
        }
    }

    @Nested
    inner class DeleteContent {
        @Test
        fun `when delete content by id expect successful 204 no content response`() {
            every { contentService.deleteContent(CONTENT_ID) } returns Unit

            mockMvc.delete("/contents/{contentId}", CONTENT_ID)
                .andExpect { status { isNoContent() } }

            verify(exactly = 1) { contentService.deleteContent(CONTENT_ID) }
        }
    }

    @Nested
    inner class DeleteContentParagraphs {
        @Test
        fun `when delete all content paragraphs expect successful 204 no content response`() {
            every { contentService.deleteContentParagraphs(CONTENT_ID) } returns Unit

            mockMvc.delete("/contents/{contentId}/paragraphs", CONTENT_ID)
                .andExpect { status { isNoContent() } }

            verify(exactly = 1) { contentService.deleteContentParagraphs(CONTENT_ID) }
        }
    }

    companion object {
        private const val CONTENT_STATUS: String = "MODERATION"
        private const val CONTENT_ID: Long = 1
    }
}
