package com.sportshoe.product.domain

import com.sportshoe.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * A product vertical, e.g. "sports-shoes" or (in the future) "baby-products".
 * See DOMAIN_MODEL.md - Category.
 */
@Entity
@Table(name = "categories")
class Category(

	@Column(name = "code", nullable = false, unique = true, length = 64)
	var code: String,

	@Column(name = "name", nullable = false, length = 128)
	var name: String,

	@Column(name = "description")
	var description: String? = null,

) : BaseEntity()
