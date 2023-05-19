package com.paragraph.ea.main.controller

import com.paragraph.ea.main.service.ContentService
import com.paragraph.ea.main.service.dto.ContentStatusEnum
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Hidden
@RestController
@RequestMapping("/admin/contents")
class AdminController(
    private val contentService: ContentService
) {
    @Hidden
    @PatchMapping("/{contentId}/accept")
    fun acceptContent(
        @PathVariable contentId: Long
    ): ResponseEntity<Void> {
        contentService.acceptContent(contentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Hidden
    @PatchMapping("/{contentId}/reject")
    fun rejectContent(
        @PathVariable contentId: Long
    ): ResponseEntity<Void> {
        contentService.rejectContent(contentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Hidden
    @PatchMapping("/{contentId}/changeStatus")
    fun changeContentStatus(
        @PathVariable contentId: Long,
        @RequestParam contentStatus: String
    ): ResponseEntity<Void> {
        contentService.changeContentStatus(contentId, ContentStatusEnum.getStatus(contentStatus))
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
