package com.sportshoe.product.domain

import com.sportshoe.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

/**
 * A retailer offer for a product: price, availability, and purchase link.
 * See DOMAIN_MODEL.md - Offer.
 */
@Entity
@Table(name = "offers")
class Offer(

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	var product: Product,

	@Column(name = "retailer", nullable = false, length = 128)
	var retailer: String,

	@Column(name = "url", nullable = false, length = 1024)
	var url: String,

	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	var price: BigDecimal,

	@Column(name = "currency", nullable = false, length = 3)
	var currency: String,

	@Column(name = "in_stock", nullable = false)
	var inStock: Boolean = true,

	@Column(name = "last_checked_at", nullable = false)
	var lastCheckedAt: Instant = Instant.now(),

) : BaseEntity()
