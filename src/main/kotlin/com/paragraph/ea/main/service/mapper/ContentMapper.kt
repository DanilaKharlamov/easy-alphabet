package com.paragraph.ea.main.service.mapper

import com.paragraph.ea.main.service.dto.ContentResponse
import com.paragraph.ea.main.service.dto.ParagraphDto
import com.paragraph.ea.main.repository.entity.ContentEntity
import com.paragraph.ea.main.repository.entity.ParagraphEntity

class ContentMapper {
    companion object {
        fun mapToContentDto(content: ContentEntity): ContentResponse {
            return ContentResponse(
                content.id,
                content.transformMode,
                content.subjectText,
                content.paragraphEntityList.map { mapToParagraphDto(it) }.toMutableList()
            )
        }

        fun mapToParagraphDto(paragraphEntity: ParagraphEntity): ParagraphDto {
            return ParagraphDto(
                nativeLetter = paragraphEntity.nativeLetter,
                learningLetter = paragraphEntity.learningLetter,
                textBody = paragraphEntity.textBody,
                inOrder = paragraphEntity.inOrder
            )
        }
    }
}
