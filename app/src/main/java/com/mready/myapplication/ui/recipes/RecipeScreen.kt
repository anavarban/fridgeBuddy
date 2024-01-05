package com.mready.myapplication.ui.recipes

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mready.myapplication.R
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.utils.LoadingAnimation
import com.mready.myapplication.ui.utils.getShareableRecipe
import com.mready.myapplication.ui.utils.shareText

@Composable
fun RecipeScreen(
    offset: Int,
    ingredients: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val recipeViewModel: RecipeViewModel = hiltViewModel()

    val uiState = recipeViewModel.uiState.collectAsState()

    val sections =
        listOf(
            "image",
            "title",
            "description",
            "nutrition",
            "yields",
            "ingredients",
            "instructions",
            "video"
        )

    val systemUiController = rememberSystemUiController()

    DisposableEffect(recipeViewModel) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false,
            isNavigationBarContrastEnforced = true,
        )

        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = true,
                isNavigationBarContrastEnforced = true,
            )
        }
    }

    LaunchedEffect(key1 = null) {
        recipeViewModel.loadRecipe(ingredients, offset)
    }

    when (uiState.value) {
        is RecipeUiState.Loading -> {
            LoadingAnimation()
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                ) {
                    sections.forEach { section ->
                        when (section) {
                            "image" -> {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    model = recipe.thumbnailUrl,
                                    contentDescription = null
                                )
                            }

                            "title" -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(.6f),
                                        text = recipe.name,
                                        textAlign = TextAlign.Left,
                                        fontSize = 24.sp,
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MainText,
                                    )

                                    if (recipe.time != null && recipe.time != 0) {
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
                                                text = recipe.time.toString() + "m",
                                                textAlign = TextAlign.Left,
                                                fontSize = 24.sp,
                                                fontFamily = Poppins,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Background
                                            )
                                        }
                                    }
                                }
                            }

                            "description" -> {
                                if (recipe.description.isNotEmpty()) {
                                    DescriptionCard(text = recipe.description)

                                }
                            }

                            "yields" -> {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp, start = 20.dp, end = 20.dp),
                                    color = com.mready.myapplication.ui.theme.Divider,
                                    thickness = 1.dp
                                )

                                if (recipe.yields.isNotEmpty() && recipe.yields != "null") {
                                    Row(
                                        modifier = Modifier.padding(
                                            top = 12.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            painter = painterResource(id = R.drawable.ic_utensils),
                                            contentDescription = null,
                                            tint = MainAccent
                                        )

                                        Text(
                                            modifier = Modifier.padding(start = 4.dp),
                                            text = recipe.yields,
                                            textAlign = TextAlign.Right,
                                            fontSize = 20.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MainAccent
                                        )
                                    }
                                }
                            }

                            "ingredients" -> {
                                val ingredients = recipe.ingredients.sortedBy { it.position }
                                    .filter { it.ingredient != "null" && it.ingredient != "n/a" }
                                LazyHorizontalGrid(
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .height(140.dp),
                                    rows = GridCells.Fixed(3),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(start = 16.dp, end = 20.dp)
                                ) {
                                    items(ingredients) { ingredient ->
                                        RecipeIngredientElement(ingredient)
                                    }
                                }

                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp, start = 20.dp, end = 20.dp),
                                    color = com.mready.myapplication.ui.theme.Divider,
                                    thickness = 1.dp
                                )
                            }

                            "nutrition" -> {
                                if (recipe.nutrition != null) {
                                    NutritionCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                        recipe = recipe
                                    )
                                }
                            }

                            "instructions" -> {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                                    text = stringResource(id = R.string.recipe_instructions),
                                    textAlign = TextAlign.Left,
                                    fontSize = 24.sp,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MainAccent,
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
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
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }

                            "video" -> {
                                if (!recipe.videoUrl.isNullOrEmpty()) {
                                    Button(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .fillMaxWidth(.8f)
                                            .padding(top = 32.dp, bottom = 16.dp),
                                        onClick = {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(recipe.videoUrl)
                                            )
                                            context.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MainAccent
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.details_go_to_video),
                                            fontSize = 20.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, start = 20.dp)
                        .shadow(4.dp, CircleShape),
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

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 32.dp, end = 20.dp)
                        .shadow(4.dp, CircleShape),
                    onClick = {
                              shareText(getShareableRecipe(recipe), context)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Background,
                        contentColor = MainAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeIngredientElement(
    ingredient: RecipeIngredient
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_ingredient),
            contentDescription = null,
            tint = MainAccent
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp),
            text = ingredient.ingredient,
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText,
        )
    }
}

@Composable
fun RecipeStepElement(
    modifier: Modifier = Modifier,
    stepNo: Int,
    stepDirections: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "$stepNo.",
            textAlign = TextAlign.Left,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainAccent
        )

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = stepDirections,
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun DescriptionCard(text: String) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var displayOverflowButton by remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 12.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Background,
            contentColor = MainText,

        ),
    ) {
        Row(
//            modifier = Modifier.padding(all = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                expanded = !expanded
                            },
                            enabled = displayOverflowButton
                        )
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    text = text,
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = {
                        displayOverflowButton = it.hasVisualOverflow || it.lineCount > 3
                    }
                )
            }

            if (displayOverflowButton) {
                Icon(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                expanded = !expanded
                            }
                        ),
                    imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }

        }
    }
}

@Composable
fun NutritionCard(
    modifier: Modifier = Modifier,
    recipe: Recipe
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Background,
            contentColor = MainText,

            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.recipe_nutrition),
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainAccent
                )

                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                expanded = !expanded
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 2.dp, end = 4.dp),
                        text = if (expanded) "Show less" else "Show more",
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = MainAccent
                    )

                    Icon(
                        modifier = Modifier,
                        imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MainAccent
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                LazyHorizontalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                expanded = !expanded
                            }
                        ),
                    rows = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 8.dp)
                ) {
                    item {
                        NutritionGridItem(
                            stringId = R.string.recipe_nutrition_calories,
                            value = recipe.nutrition!!.calories,
                            icon = R.drawable.ic_kcal
                        )
                    }

                    item {
                        NutritionGridItem(
                            stringId = R.string.recipe_nutrition_sugar,
                            value = recipe.nutrition!!.sugar,
                            icon = R.drawable.ic_sugar
                        )
                    }

                    item {
                        NutritionGridItem(
                            stringId = R.string.recipe_nutrition_carbs,
                            value = recipe.nutrition!!.carbohydrates,
                            icon = R.drawable.ic_carbs
                        )
                    }

                    item {
                        NutritionGridItem(
                            stringId = R.string.recipe_nutrition_proteins,
                            value = recipe.nutrition!!.protein,
                            icon = R.drawable.ic_proteins
                        )
                    }

                    item {
                        NutritionGridItem(
                            stringId = R.string.recipe_nutrition_fats,
                            value = recipe.nutrition!!.fat,
                            icon = R.drawable.ic_fats
                        )
                    }

                    item {
                        NutritionGridItem(
                            stringId = R.string.recipe_nutrition_fiber,
                            value = recipe.nutrition!!.fiber,
                            icon = R.drawable.ic_fibers
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionGridItem(
    stringId: Int,
    value: Int,
    icon: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MainAccent
        )

        Text(
            modifier = Modifier.padding(start = 4.dp, top = 2.dp),
            text = stringResource(
                id = stringId,
                value
            ),
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )
    }
}

