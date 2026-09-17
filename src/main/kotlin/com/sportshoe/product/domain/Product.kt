package com.sportshoe.product.domain

import com.sportshoe.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

/**
 * A category-agnostic product wrapper. Category-specific detail lives in [attributes],
 * so the same entity serves running shoes today and other categories (e.g. baby products)
 * later without schema changes. See DOMAIN_MODEL.md - Product.
 */
@Entity
@Table(name = "products")
class Product(

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	var category: Category,

	@Column(name = "name", nullable = false, length = 256)
	var name: String,

	@Column(name = "brand", nullable = false, length = 128)
	var brand: String,

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "attributes", nullable = false)
	var attributes: MutableMap<String, Any?> = mutableMapOf(),

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "images", nullable = false)
	var images: MutableList<String> = mutableListOf(),

) : BaseEntity()
