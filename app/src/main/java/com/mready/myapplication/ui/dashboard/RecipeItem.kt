package com.mready.myapplication.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.Surface

@Composable
fun RecipeItem(recipe: Recipe) {
    Card (
        modifier = Modifier.shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Surface,
            contentColor = MainText
        )
    ){
        Row (
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                model = recipe.thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = recipe.name,
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText
                )

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                        tint = MainText
                    )

                    Text(
                        modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                        text = stringResource(id = R.string.recipe_item_time_format, recipe.time?.toString() ?: "0"),
                        textAlign = TextAlign.Left,
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