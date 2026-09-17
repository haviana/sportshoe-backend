package com.sportshoe.profile.repository

import com.sportshoe.profile.domain.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserProfileRepository : JpaRepository<UserProfile, UUID>
