package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val title: String,
    val rewardDescription: String,
    val rewardType: String = "10K_MILESTONE", // "10K_MILESTONE", "GIVEAWAY", "SPECIAL_MASTERCLASS"
    val isRedeemed: Boolean = false,
    val unlockedAt: Long = System.currentTimeMillis(),
    val expiryDate: String = "Valid for 90 days"
)
