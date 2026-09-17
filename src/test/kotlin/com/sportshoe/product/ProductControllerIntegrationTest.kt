package com.sportshoe.product

import com.sportshoe.product.domain.Category
import com.sportshoe.product.domain.Offer
import com.sportshoe.product.domain.Product
import com.sportshoe.product.repository.CategoryRepository
import com.sportshoe.product.repository.OfferRepository
import com.sportshoe.product.repository.ProductRepository
import com.sportshoe.testsupport.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class ProductControllerIntegrationTest(
	@Autowired private val restTemplate: TestRestTemplate,
	@Autowired private val categoryRepository: CategoryRepository,
	@Autowired private val productRepository: ProductRepository,
	@Autowired private val offerRepository: OfferRepository,
) : AbstractIntegrationTest() {

	private fun uniqueCode(prefix: String) = "$prefix-${UUID.randomUUID().toString().take(8)}"

	private fun seedProductWithOffer(): Pair<Product, Offer> {
		val category = categoryRepository.save(
			Category(code = uniqueCode("running"), name = "Running Shoes"),
		)
		val product = productRepository.save(
			Product(
				category = category,
				name = "Test Trail Runner",
				brand = "Acme",
				attributes = mutableMapOf("terrainSuitability" to listOf("trail"), "drop" to 8),
				images = mutableListOf("https://example.com/shoe.png"),
			),
		)
		val offer = offerRepository.save(
			Offer(
				product = product,
				retailer = "Acme Store",
				url = "https://example.com/buy",
				price = BigDecimal("129.99"),
				currency = "USD",
				inStock = true,
				lastCheckedAt = Instant.now(),
			),
		)
		return product to offer
	}

	@Test
	fun `lists categories`() {
		categoryRepository.save(Category(code = uniqueCode("cat"), name = "A Category"))

		val response = restTemplate.getForEntity("/api/categories", String::class.java)

		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(response.body).contains("\"code\"")
	}

	@Test
	fun `gets a product by id with its offers`() {
		val (product, offer) = seedProductWithOffer()

		val response = restTemplate.getForEntity("/api/products/${product.id}", String::class.java)

		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(response.body).contains("Test Trail Runner")
		assertThat(response.body).contains(offer.retailer)
	}

	@Test
	fun `lists products filtered by category code`() {
		val (product, _) = seedProductWithOffer()

		val response = restTemplate.getForEntity(
			"/api/products?categoryCode=${product.category.code}",
			String::class.java,
		)

		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(response.body).contains(product.name)
	}

	@Test
	fun `returns 404 with standard error body for unknown product`() {
		val response = restTemplate.getForEntity("/api/products/${UUID.randomUUID()}", String::class.java)

		assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
		assertThat(response.body).contains("\"status\":404")
		assertThat(response.body).contains("/api/products/")
	}
}
