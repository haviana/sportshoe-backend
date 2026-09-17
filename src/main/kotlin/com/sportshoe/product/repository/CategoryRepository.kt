package com.sportshoe.product.repository

import com.sportshoe.product.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface CategoryRepository : JpaRepository<Category, UUID> {
	fun findByCode(code: String): Optional<Category>
}
