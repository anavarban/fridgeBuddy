package com.mready.myapplication.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.Surface

@Composable
fun RecipeItem(
    recipe: Recipe,
    baseIngredient: String,
    modifier: Modifier = Modifier.width(240.dp),
    onClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick(baseIngredient) },
        colors = CardDefaults.cardColors(
            containerColor = Surface,
            contentColor = MainText
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
//            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
//            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = recipe.thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .fillMaxSize()
                    .background(Brush.horizontalGradient(0.0f to Color.Transparent, 1.0f to Surface))
                    .padding(start = 28.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .fillMaxWidth(.6f)
                        .padding(top = 12.dp, end = 8.dp),
                    text = recipe.name,
                    textAlign = TextAlign.Right,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText,
                    minLines = 3,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp, end = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                        tint = MainText
                    )

                    Text(
                        modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                        text = stringResource(
                            id = R.string.recipe_item_time_format,
                            recipe.time?.toString() ?: "?"
                        ),
                        textAlign = TextAlign.Right,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = MainText
                    )
                }

                if (recipe.yields.isNotEmpty() && recipe.yields != "null") {
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp, end = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_utensils),
                            contentDescription = null,
                            tint = MainText
                        )

                        Text(
                            modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                            text = recipe.yields,
                            textAlign = TextAlign.Right,
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = MainText
                        )
                    }
                }
            }
        }
    }
}