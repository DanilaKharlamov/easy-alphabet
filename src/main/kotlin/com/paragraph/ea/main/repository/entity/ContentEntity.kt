package com.paragraph.ea.main.repository.entity

import com.paragraph.ea.main.service.dto.ContentStatusEnum
import com.paragraph.ea.main.service.dto.TransformModeEnum
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "content")
class ContentEntity(

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: ContentStatusEnum = ContentStatusEnum.MODERATION,

    @Enumerated(EnumType.STRING)
    @Column(name = "transform_mode", nullable = false)
    var transformMode: TransformModeEnum = TransformModeEnum.ALPHABET,

    @Column(name = "off_set", nullable = false)
    var offSet: Int = 0,

    @Column(name = "subject_text", nullable = false)
    var subjectText: String = "",

    @Column(name = "text_language", nullable = false)
    var textLanguage: String = "",

    @OneToMany(
        mappedBy = "content",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @OrderBy("in_order")
    var paragraphEntityList: MutableSet<ParagraphEntity> = mutableSetOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0
}