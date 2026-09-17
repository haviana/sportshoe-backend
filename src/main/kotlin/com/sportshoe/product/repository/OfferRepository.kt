package com.sportshoe.product.repository

import com.sportshoe.product.domain.Offer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OfferRepository : JpaRepository<Offer, UUID> {
	fun findByProductId(productId: UUID): List<Offer>
}
