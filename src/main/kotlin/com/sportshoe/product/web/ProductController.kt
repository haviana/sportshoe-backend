package com.sportshoe.product.web

import com.sportshoe.common.web.ResourceNotFoundException
import com.sportshoe.product.dto.CategoryResponse
import com.sportshoe.product.dto.ProductResponse
import com.sportshoe.product.repository.CategoryRepository
import com.sportshoe.product.repository.OfferRepository
import com.sportshoe.product.repository.ProductRepository
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ProductController(
	private val categoryRepository: CategoryRepository,
	private val productRepository: ProductRepository,
	private val offerRepository: OfferRepository,
) {

	@GetMapping("/api/categories")
	fun listCategories(): List<CategoryResponse> =
		categoryRepository.findAll().map { CategoryResponse.from(it) }

	@GetMapping("/api/products")
	fun listProducts(
		@RequestParam(name = "categoryCode", required = false) categoryCode: String?,
		@RequestParam(name = "page", defaultValue = "0") page: Int,
		@RequestParam(name = "size", defaultValue = "20") size: Int,
	): List<ProductResponse> {
		val pageable = PageRequest.of(page, size)
		val products = if (categoryCode != null) {
			productRepository.findByCategoryCode(categoryCode, pageable)
		} else {
			productRepository.findAll(pageable)
		}
		return products.content.map { ProductResponse.from(it, offerRepository.findByProductId(it.id!!)) }
	}

	@GetMapping("/api/products/{id}")
	fun getProduct(@PathVariable id: UUID): ProductResponse {
		val product = productRepository.findById(id)
			.orElseThrow { ResourceNotFoundException("Product $id not found") }
		return ProductResponse.from(product, offerRepository.findByProductId(id))
	}
}
