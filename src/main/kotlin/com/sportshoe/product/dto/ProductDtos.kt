package com.sportshoe.product.dto

import com.sportshoe.product.domain.Category
import com.sportshoe.product.domain.Offer
import com.sportshoe.product.domain.Product
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class CategoryResponse(
	val id: UUID,
	val code: String,
	val name: String,
	val description: String?,
) {
	companion object {
		fun from(category: Category) = CategoryResponse(
			id = category.id!!,
			code = category.code,
			name = category.name,
			description = category.description,
		)
	}
}

data class OfferResponse(
	val id: UUID,
	val retailer: String,
	val url: String,
	val price: BigDecimal,
	val currency: String,
	val inStock: Boolean,
	val lastCheckedAt: Instant,
) {
	companion object {
		fun from(offer: Offer) = OfferResponse(
			id = offer.id!!,
			retailer = offer.retailer,
			url = offer.url,
			price = offer.price,
			currency = offer.currency,
			inStock = offer.inStock,
			lastCheckedAt = offer.lastCheckedAt,
		)
	}
}

data class ProductResponse(
	val id: UUID,
	val name: String,
	val brand: String,
	val category: CategoryResponse,
	val attributes: Map<String, Any?>,
	val images: List<String>,
	val offers: List<OfferResponse> = emptyList(),
) {
	companion object {
		fun from(product: Product, offers: List<Offer> = emptyList()) = ProductResponse(
			id = product.id!!,
			name = product.name,
			brand = product.brand,
			category = CategoryResponse.from(product.category),
			attributes = product.attributes,
			images = product.images,
			offers = offers.map { OfferResponse.from(it) },
		)
	}
}
