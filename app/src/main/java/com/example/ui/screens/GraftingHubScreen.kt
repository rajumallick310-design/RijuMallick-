package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class GraftingTechnique(
    val title: String,
    val plantType: String,
    val bestSeason: String,
    val difficulty: String,
    val successRate: String,
    val drawableName: String,
    val summary: String,
    val requiredTools: List<String>,
    val stepByStep: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraftingHubScreen() {
    val context = LocalContext.current
    var selectedTechnique by remember { mutableStateOf<GraftingTechnique?>(null) }

    val techniques = remember {
        listOf(
            GraftingTechnique(
                title = "Mango Cleft / Wedge Grafting",
                plantType = "Mango, Avocado, Sapota",
                bestSeason = "Late Spring & Early Monsoon (Feb - July)",
                difficulty = "Medium",
                successRate = "85% - 92%",
                drawableName = "img_mango_graft_1784810626465",
                summary = "The cleft graft is the most popular fruit tree grafting method. A scion with 2-3 dormant buds is wedged into a split rootstock.",
                requiredTools = listOf("Sharp secateurs", "Grafting knife", "Parafilm / UV Graft tape", "Plastic cap sleeve"),
                stepByStep = listOf(
                    "Cut rootstock stem horizontally at 6-8 inches above soil level.",
                    "Make a 1.5 inch vertical split down the center of the rootstock.",
                    "Whittle both sides of the scion base into a clean 45-degree wedge.",
                    "Insert scion wedge into the cleft split, ensuring green cambium lines up.",
                    "Wrap tightly with UV grafting film and seal against rain moisture."
                )
            ),
            GraftingTechnique(
                title = "Rose T-Budding (Shield Grafting)",
                plantType = "Hybrid Tea Roses, Floribunda",
                bestSeason = "Winter to Spring (Nov - March)",
                difficulty = "Beginner Friendly",
                successRate = "90% - 95%",
                drawableName = "img_garden_banner_1784810613294",
                summary = "T-Budding uses a single dormant eye (bud) inserted underneath a T-shaped slit in the bark of wild rose rootstock.",
                requiredTools = listOf("Budding knife", "Raffia / Rubber budding strip", "Dormant rose budstick"),
                stepByStep = listOf(
                    "Slice a vertical 1-inch cut with a horizontal top line ('T' shape) on rootstock bark.",
                    "Gently peel back bark flaps using the knife bark opener.",
                    "Slice a plump eye bud with a thin shield of wood from the donor stem.",
                    "Slide shield bud under the bark flaps until snug.",
                    "Wrap rubber strip around above and below the bud eye."
                )
            ),
            GraftingTechnique(
                title = "Air Layering (Guttee Technique)",
                plantType = "Guava, Lemon, Citrus, Lychee, Ficus",
                bestSeason = "Monsoon Season (June - September)",
                difficulty = "Easy",
                successRate = "95%",
                drawableName = "img_grafting_hero_1784810598121",
                summary = "Air layering forces roots to form directly on a live branch while still attached to the parent tree.",
                requiredTools = listOf("Pruning knife", "Sphagnum moss / Coconut coir", "Clear plastic sheet", "Jute string / Twine"),
                stepByStep = listOf(
                    "Select a 1-year-old healthy branch about pencil thickness.",
                    "Remove a 1-inch ring of bark, scraping off green cambium to bare wood.",
                    "Apply organic rooting gel or aloe vera paste to the upper cut.",
                    "Enclose ring in soaked, squeezed sphagnum moss ball.",
                    "Wrap tightly in clear plastic sheet and tie both ends firmly."
                )
            ),
            GraftingTechnique(
                title = "Succulents & Cactus Flat Grafting",
                plantType = "Moon Cactus, Euphorbia, Gymnocalycium",
                bestSeason = "All Year Round (Indoor Controlled)",
                difficulty = "Beginner",
                successRate = "88%",
                drawableName = "img_succulent_care_1784810668000",
                summary = "Flat grafting combines colorful non-photosynthetic cactus scions with hardy green dragon fruit rootstock.",
                requiredTools = listOf("Sterilized razor blade", "Rubber bands", "Isopropyl alcohol"),
                stepByStep = listOf(
                    "Sterilize razor blade with alcohol rub.",
                    "Slice top off green rootstock cleanly in one horizontal swipe.",
                    "Slice bottom off colorful scion ball.",
                    "Press scion onto rootstock center, twisting slightly to push out air bubbles.",
                    "Secure firmly with crossed rubber bands over the top for 10 days."
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Grafting Techniques Hub",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Master Plant Grafting 🌱",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Learn step-by-step techniques to combine rootstock hardiness with high-yield fruit & flower varieties.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            items(techniques) { technique ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedTechnique = technique }
                        .testTag("technique_card_${technique.title}")
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = ImageUtils.getDrawableResId(context, technique.drawableName)),
                                contentDescription = technique.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Surface(
                                color = Color.Black.copy(alpha = 0.75f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Success Rate: ${technique.successRate}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFC107),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = technique.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Park,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = technique.plantType,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = technique.summary,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { selectedTechnique = technique },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("View Full Guide & Tools")
                            }
                        }
                    }
                }
            }
        }

        // Selected Technique Modal Sheet
        selectedTechnique?.let { tech ->
            AlertDialog(
                onDismissRequest = { selectedTechnique = null },
                confirmButton = {
                    TextButton(onClick = { selectedTechnique = null }) {
                        Text("Close Guide", fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ContentCut, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tech.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "📅 Best Season: ${tech.bestSeason}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        item {
                            Text(
                                text = "🧰 Required Tool Kit:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
                                tech.requiredTools.forEach { tool ->
                                    Text("• $tool", fontSize = 13.sp)
                                }
                            }
                        }

                        item {
                            Text(
                                text = "📝 Step-by-Step Instructions:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(tech.stepByStep.size) { idx ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${idx + 1}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = tech.stepByStep[idx],
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
