package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "current_user",
    val phoneNumber: String = "",
    val email: String = "",
    val authType: String = "PHONE", // "PHONE" or "GOOGLE"
    val displayName: String = "Green Thumb Expert",
    val username: String = "@sabuj_gardener",
    val bio: String = "Passionate plant grafted & organic fruit grower 🌱 Sharing daily grafting tips & plant care guides!",
    val avatarUrl: String = "",
    val passwordPin: String = "1234",
    val followerCount: Int = 9980, // Close to 10k so user can easily test the 10,000 follower milestone reward trigger!
    val followingCount: Int = 142,
    val isFollowing: Boolean = false,
    val hasReached10k: Boolean = false,
    val rewardCouponCode: String = "",
    val plantBadges: String = "Master Grafter, Rose Hybridizer, Organic Composter",
    val joinDate: String = "July 2026",
    val isLoggedIn: Boolean = true
)
