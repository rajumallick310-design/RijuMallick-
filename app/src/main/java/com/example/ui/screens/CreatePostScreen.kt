package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit,
    onSubmitPost: (
        postType: String,
        category: String,
        title: String,
        content: String,
        mediaDrawableName: String,
        graftingSteps: String
    ) -> Unit
) {
    var postType by remember { mutableStateOf("GRAFT_GUIDE") } // "PHOTO", "VIDEO", "GRAFT_GUIDE"
    var category by remember { mutableStateOf("Grafting") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var graftingStepsText by remember { mutableStateOf("") }
    var selectedMediaDrawable by remember { mutableStateOf("img_grafting_hero_1784810598121") }

    val context = LocalContext.current
    val categories = listOf("Grafting", "Plant Care", "Propagation", "Pest Control", "Succulents", "Video Guides")
    val availableImages = listOf(
        "img_grafting_hero_1784810598121",
        "img_mango_graft_1784810626465",
        "img_garden_banner_1784810613294",
        "img_succulent_care_1784810668000"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publish Grafting Tip / Post 🌱", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Post Type Segmented Selector
            Text("Select Post Format:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp)
            ) {
                listOf(
                    Triple("GRAFT_GUIDE", "Graft Guide", Icons.Default.ContentCut),
                    Triple("VIDEO", "Video Post", Icons.Default.Videocam),
                    Triple("PHOTO", "Photo Post", Icons.Default.Image)
                ).forEach { (type, label, icon) ->
                    val isSelected = postType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable { postType = type }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Category Selector
            Text("Category Tag:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title (e.g. Mango Cleft Grafting Tips)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("post_title_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Content Description Input
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Description & Care Tips") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("post_content_input"),
                shape = RoundedCornerShape(12.dp)
            )

            // Optional Grafting Step-by-Step Instructions
            if (postType == "GRAFT_GUIDE" || postType == "VIDEO") {
                OutlinedTextField(
                    value = graftingStepsText,
                    onValueChange = { graftingStepsText = it },
                    label = { Text("Step-by-Step Instructions (Separate steps with '|')") },
                    placeholder = { Text("1. Prepare Rootstock | 2. Cut Wedge | 3. Align Cambium | 4. Wrap Tape") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("post_steps_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Select Media Thumbnail
            Text("Select Plant Photo / Thumbnail:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableImages) { imgName ->
                    val isSelected = selectedMediaDrawable == imgName
                    Box(
                        modifier = Modifier
                            .size(100.dp, 80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedMediaDrawable = imgName }
                    ) {
                        Image(
                            painter = painterResource(id = ImageUtils.getDrawableResId(context, imgName)),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Submit Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSubmitPost(
                            postType,
                            category,
                            title,
                            content,
                            selectedMediaDrawable,
                            graftingStepsText
                        )
                        onNavigateBack()
                    }
                },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_post_button")
            ) {
                Icon(Icons.Default.Publish, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publish to SabujKatha Feed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
