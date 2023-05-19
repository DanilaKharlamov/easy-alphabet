package com.paragraph.ea.main.repository.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "paragraph")
data class ParagraphEntity(

    @Column(name = "text_body", nullable = false)
    @Type(type = "text")
    var textBody: String,

    @Column(name = "native_letter", nullable = false)
    var nativeLetter: String,

    @Column(name = "learning_letter", nullable = false)
    var learningLetter: String,

    @Column(name = "in_order", nullable = false)
    var inOrder: Int,

    @ManyToOne(
        cascade = [CascadeType.REFRESH],
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "content.id")
    @JsonIgnore
    var content: ContentEntity
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}