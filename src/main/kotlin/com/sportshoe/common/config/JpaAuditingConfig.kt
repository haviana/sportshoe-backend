package com.sportshoe.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * Enables JPA auditing (createdAt/updatedAt) across all entities extending BaseEntity.
 */
@Configuration
@EnableJpaAuditing
class JpaAuditingConfig
