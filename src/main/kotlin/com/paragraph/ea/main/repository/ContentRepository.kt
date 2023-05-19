package com.paragraph.ea.main.repository

import com.paragraph.ea.main.service.dto.ContentStatusEnum
import com.paragraph.ea.main.repository.entity.ContentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : JpaRepository<ContentEntity, Long> {

    fun getByStatus(status: ContentStatusEnum): List<ContentEntity>

    fun getByStatusAndTextLanguage(status: ContentStatusEnum, language: String): List<ContentEntity>
}