package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CommentEntity
import com.example.data.local.CouponEntity
import com.example.data.local.PostEntity
import com.example.data.local.SabujKathaRepository
import com.example.data.local.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SabujKathaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SabujKathaRepository

    val currentUser: StateFlow<UserEntity?>
    val selectedCategory = MutableStateFlow("All")

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: StateFlow<List<PostEntity>>

    val bookmarkedPosts: StateFlow<List<PostEntity>>
    val coupons: StateFlow<List<CouponEntity>>

    private val _selectedPostForComments = MutableStateFlow<PostEntity?>(null)
    val selectedPostForComments: StateFlow<PostEntity?> = _selectedPostForComments.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val commentsForSelectedPost: StateFlow<List<CommentEntity>>

    private val _selectedPostForDetail = MutableStateFlow<PostEntity?>(null)
    val selectedPostForDetail: StateFlow<PostEntity?> = _selectedPostForDetail.asStateFlow()

    private val _unlockedRewardCoupon = MutableStateFlow<CouponEntity?>(null)
    val unlockedRewardCoupon: StateFlow<CouponEntity?> = _unlockedRewardCoupon.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        val database = AppDatabase.getInstance(application)
        repository = SabujKathaRepository(database)

        currentUser = repository.currentUser.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        posts = selectedCategory.flatMapLatest { category ->
            repository.getPostsByCategory(category)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        bookmarkedPosts = repository.bookmarkedPosts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        coupons = repository.allCoupons.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        commentsForSelectedPost = _selectedPostForComments.flatMapLatest { post ->
            if (post != null) {
                repository.getCommentsForPost(post.id)
            } else {
                MutableStateFlow(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Initialize default seed data
        viewModelScope.launch {
            repository.initSeedDataIfNeeded()
        }
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
    }

    fun toggleLike(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleLike(post)
        }
    }

    fun toggleBookmark(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(post)
            showSnackbar(if (!post.isBookmarked) "Post saved to Bookmarks" else "Removed from Bookmarks")
        }
    }

    fun openCommentsForPost(post: PostEntity?) {
        _selectedPostForComments.value = post
    }

    fun openPostDetail(post: PostEntity?) {
        _selectedPostForDetail.value = post
    }

    fun addComment(postId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(postId, text.trim())
        }
    }

    fun createPost(
        postType: String,
        category: String,
        title: String,
        content: String,
        mediaDrawableName: String,
        graftingSteps: String = ""
    ) {
        viewModelScope.launch {
            repository.createPost(postType, category, title, content, mediaDrawableName, graftingSteps)
            showSnackbar("🌱 Your post has been published to the SabujKatha feed!")
        }
    }

    fun addFollowersAndCheckReward(countToAdd: Int) {
        viewModelScope.launch {
            val generatedCoupon = repository.addFollowers(countToAdd)
            if (generatedCoupon != null) {
                _unlockedRewardCoupon.value = generatedCoupon
            } else {
                showSnackbar("🎉 Added +$countToAdd followers to your profile!")
            }
        }
    }

    fun dismissRewardDialog() {
        _unlockedRewardCoupon.value = null
    }

    fun updateProfile(name: String, username: String, bio: String, pin: String, badges: String) {
        viewModelScope.launch {
            repository.updateProfile(name, username, bio, pin, badges)
            showSnackbar("Profile updated successfully!")
        }
    }

    fun loginOrSignUp(phoneOrEmail: String, authType: String, pin: String, name: String = "Sabuj Gardener") {
        viewModelScope.launch {
            repository.loginOrSignUp(phoneOrEmail, authType, pin, name)
            showSnackbar("Welcome to SabujKatha, $name! 🌱")
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            showSnackbar("Signed out successfully.")
        }
    }

    fun redeemCoupon(code: String) {
        viewModelScope.launch {
            repository.redeemCoupon(code)
            showSnackbar("Coupon $code redeemed successfully! Enjoy your reward!")
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
