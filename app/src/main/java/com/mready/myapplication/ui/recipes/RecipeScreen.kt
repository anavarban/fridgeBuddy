package com.mready.myapplication.ui.recipes

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    ingredients: String,
    onBackClick: () -> Unit
) {
    val recipeViewModel: RecipeViewModel = hiltViewModel()

    val uiState = recipeViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = null) {
        recipeViewModel.loadRecipe(ingredients)
    }

    when (uiState.value) {
        is RecipeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText
                )
            }
        }

        is RecipeUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error",
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText
                )
            }
        }

        is RecipeUiState.Success -> {

            val recipe = (uiState.value as RecipeUiState.Success).recipe

            val sheetState = rememberModalBottomSheetState()
            LaunchedEffect(key1 = null) {
                sheetState.show()
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = recipe.thumbnailUrl,
                    contentDescription = null
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, start = 20.dp),
                    onClick = onBackClick,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Background,
                        contentColor = MainAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.75f)
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(20.dp),
                    color = Background,

                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(.5f),
                                text = recipe.name,
                                textAlign = TextAlign.Left,
                                fontSize = 24.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold,
                                color = MainText,
                                minLines = 2,
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MainAccent),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(start = 4.dp),
                                    painter = painterResource(id = R.drawable.ic_time),
                                    contentDescription = null,
                                    tint = Background
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(top = 4.dp, start = 4.dp, end = 4.dp),
                                    text = (recipe.time?.toString() ?: "? ") + "m",
                                    textAlign = TextAlign.Left,
                                    fontSize = 24.sp,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Background
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            recipe.instructions.sortedBy {
                                it.position
                            }.forEach {
                                RecipeStepElement(
                                    stepNo = it.position,
                                    stepDirections = it.displayText,
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun RecipeStepElement(
    modifier: Modifier = Modifier,
    stepNo: Int,
    stepDirections: String,
) {
    var expanded by remember {
        //todo first index could start expanded?
        mutableStateOf(false)
    }

    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 20.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    //todo this kinda works but i want only one expanded at a time
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = extraPadding.coerceAtLeast(0.dp))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                expanded = !expanded
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "$stepNo.",
            textAlign = TextAlign.Left,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = if (expanded) MainAccent else LightAccent
        )

        Text(
            text = stepDirections,
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = if (expanded) MainText else SecondaryText,
            maxLines = if (expanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Clip
        )
    }
}
