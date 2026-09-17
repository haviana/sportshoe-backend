package com.sportshoe.product.repository

import com.sportshoe.product.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID> {
	fun findByCategoryCode(categoryCode: String, pageable: Pageable): Page<Product>
}
