package com.example.data.local

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.CommentEntity
import com.example.data.local.CouponEntity
import com.example.data.local.PostEntity
import com.example.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SabujKathaRepository(private val db: AppDatabase) {

    val currentUser: Flow<UserEntity?> = db.userDao().getUserById("current_user")
    val allPosts: Flow<List<PostEntity>> = db.postDao().getAllPosts()
    val bookmarkedPosts: Flow<List<PostEntity>> = db.postDao().getBookmarkedPosts()
    val allCoupons: Flow<List<CouponEntity>> = db.couponDao().getAllCoupons()

    fun getPostsByCategory(category: String): Flow<List<PostEntity>> {
        return if (category == "All") {
            db.postDao().getAllPosts()
        } else {
            db.postDao().getPostsByCategory(category)
        }
    }

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> {
        return db.commentDao().getCommentsForPost(postId)
    }

    suspend fun initSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        val existingUser = db.userDao().getUserOnce("current_user")
        if (existingUser == null) {
            val defaultUser = UserEntity(
                id = "current_user",
                phoneNumber = "+91 98765 43210",
                email = "gardener.sabuj@gmail.com",
                authType = "PHONE",
                displayName = "Ananya Roy",
                username = "@ananya_sabuj",
                bio = "Urban Gardener & Fruit Grafting Enthusiast 🌿 Mango, Citrus & Rose grafting tutorials. Join my green journey!",
                passwordPin = "1234",
                followerCount = 9980, // 20 away from 10,000 for quick testing!
                followingCount = 184,
                hasReached10k = false,
                rewardCouponCode = "",
                isLoggedIn = true
            )
            db.userDao().insertOrUpdateUser(defaultUser)
        }

        if (db.postDao().getPostCount() == 0) {
            val samplePosts = listOf(
                PostEntity(
                    id = 1,
                    authorId = "current_user",
                    authorName = "Ananya Roy",
                    authorUsername = "@ananya_sabuj",
                    postType = "VIDEO",
                    category = "Grafting",
                    title = "🥭 Cleft Grafting 2-Year Mango Branch - High Success Method!",
                    content = "Step-by-step tutorial on grafting Amrapali scion onto local country mango rootstock. Notice how the cambium layers line up perfectly on one side. Wrap tightly with grafting tape to keep moisture locked in!",
                    mediaDrawableName = "img_mango_graft_1784810626465",
                    likeCount = 384,
                    commentCount = 28,
                    isLiked = true,
                    videoDurationSec = 52,
                    graftingSteps = "1. Cut rootstock stem horizontally at pencil thickness.|2. Split vertical center 1.5 inches deep.|3. Cut scion into dual 45-degree wedges.|4. Align cambium layers on at least one side.|5. Wrap tightly with UV grafting film and cap with plastic sleeve."
                ),
                PostEntity(
                    id = 2,
                    authorId = "creator_raj",
                    authorName = "Rajesh Gardener",
                    authorUsername = "@raj_greenhouse",
                    postType = "PHOTO",
                    category = "Pest Control",
                    title = "🌿 Homemade Garlic & Neem Liquid Spray for Aphid Control",
                    content = "Say goodbye to synthetic insecticides! Boil 50g crushed garlic + neem leaves in 1 liter water. Dilute 1:10 and spray on undersides of leaves early in the morning.",
                    mediaDrawableName = "img_garden_banner_1784810613294",
                    likeCount = 215,
                    commentCount = 14,
                    isLiked = false,
                    isBookmarked = true
                ),
                PostEntity(
                    id = 3,
                    authorId = "creator_priya",
                    authorName = "Priya Succulents",
                    authorUsername = "@priya_succulents",
                    postType = "PHOTO",
                    category = "Succulents",
                    title = "🌵 Leaf Cuttings Propagation in Dry Climate",
                    content = "Let succulent leaf cuttings dry in shade for 3-4 days until callus forms before placing on well-draining cactus mix. Avoid heavy watering until tiny pink roots sprout!",
                    mediaDrawableName = "img_succulent_care_1784810668000",
                    likeCount = 192,
                    commentCount = 9,
                    isLiked = false
                ),
                PostEntity(
                    id = 4,
                    authorId = "creator_bengal",
                    authorName = "Bengal Plant Care",
                    authorUsername = "@bengal_plants",
                    postType = "VIDEO",
                    category = "Propagation",
                    title = "🌳 Air Layering (Guttee) Technique for Guava & Lemon",
                    content = "Monsoon is the best season for air layering! Ring bark 1 inch wide, apply cinnamon or aloe vera gel as natural rooting hormone, and wrap with moist moss.",
                    mediaDrawableName = "img_grafting_hero_1784810598121",
                    likeCount = 512,
                    commentCount = 42,
                    isLiked = true,
                    videoDurationSec = 75,
                    graftingSteps = "1. Choose healthy 1-year-old pencil branch.|2. Remove 1-inch outer ring of bark down to green wood.|3. Apply rooting hormone or aloe vera paste.|4. Wrap tightly with moist sphagnum moss.|5. Tie securely with plastic sheet and string."
                )
            )
            db.postDao().insertPosts(samplePosts)

            // Seed sample comments
            val sampleComments = listOf(
                CommentEntity(postId = 1, authorName = "Debashis M.", text = "Outstanding tutorial! Did this on my terrace mango tree last week and buds are opening! 🌱"),
                CommentEntity(postId = 1, authorName = "Suman K.", text = "What month is ideal for cleft grafting in South Asia?"),
                CommentEntity(postId = 1, authorName = "Ananya Roy", text = "Late February before spring flush or July during monsoon season works best!"),
                CommentEntity(postId = 2, authorName = "Meera S.", text = "Tried the garlic neem spray on my brinjal plants and bugs disappeared in 2 days! Thank you!")
            )
            db.commentDao().insertComments(sampleComments)
        }
    }

    suspend fun createPost(
        postType: String,
        category: String,
        title: String,
        content: String,
        mediaDrawableName: String,
        graftingSteps: String = ""
    ) = withContext(Dispatchers.IO) {
        val user = db.userDao().getUserOnce("current_user")
        val newPost = PostEntity(
            authorId = user?.id ?: "current_user",
            authorName = user?.displayName ?: "Ananya Roy",
            authorUsername = user?.username ?: "@ananya_sabuj",
            postType = postType,
            category = category,
            title = title,
            content = content,
            mediaDrawableName = if (mediaDrawableName.isEmpty()) "img_garden_banner_1784810613294" else mediaDrawableName,
            graftingSteps = graftingSteps,
            videoDurationSec = if (postType == "VIDEO") 45 else 0,
            likeCount = 1,
            isLiked = true
        )
        db.postDao().insertPost(newPost)
    }

    suspend fun toggleLike(post: PostEntity) = withContext(Dispatchers.IO) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likeCount + 1 else (post.likeCount - 1).coerceAtLeast(0)
        db.postDao().updateLike(post.id, newLiked, newCount)
    }

    suspend fun toggleBookmark(post: PostEntity) = withContext(Dispatchers.IO) {
        db.postDao().updateBookmark(post.id, !post.isBookmarked)
    }

    suspend fun addComment(postId: Long, text: String) = withContext(Dispatchers.IO) {
        val user = db.userDao().getUserOnce("current_user")
        val comment = CommentEntity(
            postId = postId,
            authorName = user?.displayName ?: "Ananya Roy",
            text = text
        )
        db.commentDao().insertComment(comment)
        db.postDao().incrementCommentCount(postId)
    }

    suspend fun updateProfile(name: String, username: String, bio: String, pin: String, badges: String) = withContext(Dispatchers.IO) {
        db.userDao().updateProfile("current_user", name, username, bio, pin, badges)
    }

    suspend fun addFollowers(countToAdd: Int): CouponEntity? = withContext(Dispatchers.IO) {
        val user = db.userDao().getUserOnce("current_user") ?: return@withContext null
        val newCount = user.followerCount + countToAdd
        var generatedCoupon: CouponEntity? = null
        var has10k = user.hasReached10k
        var couponCode = user.rewardCouponCode

        if (newCount >= 10000 && !has10k) {
            has10k = true
            val randomSuffix = Random.nextInt(1000, 9999)
            couponCode = "SABUJ-10K-GOLD-$randomSuffix"

            generatedCoupon = CouponEntity(
                code = couponCode,
                title = "🏆 10,000 Follower Creator Reward!",
                rewardDescription = "Congratulations on reaching 10K Followers! Unlock access to Private Grafting Masterclasses & 1 Year Free Seed Box Giveaways.",
                rewardType = "10K_MILESTONE",
                isRedeemed = false,
                unlockedAt = System.currentTimeMillis()
            )
            db.couponDao().insertCoupon(generatedCoupon)
        }

        db.userDao().updateFollowerCount("current_user", newCount, has10k, couponCode)
        return@withContext generatedCoupon
    }

    suspend fun loginOrSignUp(phoneOrEmail: String, authType: String, pin: String, name: String = "Green Gardener") = withContext(Dispatchers.IO) {
        val user = UserEntity(
            id = "current_user",
            phoneNumber = if (authType == "PHONE") phoneOrEmail else "+91 98765 43210",
            email = if (authType == "GOOGLE") phoneOrEmail else "gardener@sabuj.com",
            authType = authType,
            displayName = if (name.isNotBlank()) name else "Sabuj Gardener",
            username = "@${name.lowercase().replace(" ", "_")}",
            bio = "Gardening & Plant Care lover 🌿",
            passwordPin = if (pin.isNotBlank()) pin else "1234",
            followerCount = 9980,
            isLoggedIn = true
        )
        db.userDao().insertOrUpdateUser(user)
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        db.userDao().updateLoginState("current_user", false)
    }

    suspend fun redeemCoupon(code: String) = withContext(Dispatchers.IO) {
        db.couponDao().markAsRedeemed(code)
    }
}
