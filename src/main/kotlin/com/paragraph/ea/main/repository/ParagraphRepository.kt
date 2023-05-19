package com.paragraph.ea.main.repository

import com.paragraph.ea.main.repository.entity.ContentEntity
import com.paragraph.ea.main.repository.entity.ParagraphEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParagraphRepository: JpaRepository<ParagraphEntity, Long>{

    fun findAllByContent(content: ContentEntity) : List<ParagraphEntity>

}