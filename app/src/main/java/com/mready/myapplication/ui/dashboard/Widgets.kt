package com.mready.myapplication.ui.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.OpenYouTubeChannel
import com.mready.myapplication.ui.utils.openYoutubeLink


@Composable
fun PopularRecipes(
    recipeUrls: List<String>
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.dashboard_popular_recipes),
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        OpenYouTubeChannel("https://www.youtube.com/@JamilaCuisine", context)
                    },
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.generic_see_more),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MainAccent
            )
        }
    }

    LazyRow(
        modifier = Modifier.padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp)
    ) {
        items(recipeUrls) {
            YoutubeScreen(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(width = 320.dp, height = 180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                videoId = it
            )
        }
    }
}

@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier
) {
    val context = LocalContext.current
    Box(modifier = modifier) {
        val imgUrl = "https://i.ytimg.com/vi/${videoId}/hqdefault.jpg"
        val intentUrl = "watch?v=${videoId}&pp=ygUGamFtaWxh"

        AsyncImage(
            modifier = modifier.matchParentSize(),
            model = imgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Icon(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        openYoutubeLink(context, intentUrl)
                    }
                )
                .border(
                    width = 4.dp,
                    color = Background,
                    shape = CircleShape
                ),
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
            tint = Background
        )
    }
}


@Composable
fun RecommendedRecipes(
    recipes: List<Recipe>,
    ingredientsToExpire: List<String>,
    onRecipeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.dashboard_previous_recipes),
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Text(
            modifier = Modifier.alpha(.6f),
            text = stringResource(id = R.string.dashboard_rec_recipes_subtitle),
            textAlign = TextAlign.Left,
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 20.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        recipes.forEach {
            RecipeItem(
                modifier = Modifier
                    .width(240.dp)
                    .height(180.dp),
                recipe = it,
                baseIngredient = if (ingredientsToExpire.isNotEmpty()) ingredientsToExpire[recipes.indexOf(
                    it
                )] else "",
                onClick = onRecipeClick
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun FridgeIngredients(
    ingredients: List<Ingredient>,
    onSeeFridgeClick: () -> Unit,
    onIngredientClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.dashboard_your_fridge),
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onSeeFridgeClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.generic_see_all),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MainAccent
            )
        }
    }

    if (ingredients.isEmpty()) {
        Text(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            text = stringResource(id = R.string.dashboard_no_ingredients),
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )
    } else {
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ingredients.forEach {
                IngredientItem(
                    modifier = Modifier
                        .width(160.dp)
                        .clickable { onIngredientClick(it.id) },
                    ingredient = it,
                    showDeleteButton = false
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }


}

