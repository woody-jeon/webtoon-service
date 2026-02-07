package com.lezhin.webtoonservice.storage.db.core

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    @Comment("생성일시")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Comment("수정일시")
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set
}
