package com.mready.myapplication.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins

@Composable
fun IngredientItem(
    modifier: Modifier = Modifier.width(160.dp),
    ingredient: Ingredient,
    showDeleteButton: Boolean = true,
    onDeleteClick: () -> Unit = {}
) {

    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Background,
            contentColor = MainText
        )
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
        ){

            AsyncImage(
                modifier = Modifier
                    .aspectRatio(3/4f),
                model = ingredient.image,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(0.0f to Color.Transparent, 1.0f to Background))
                    .align(Alignment.BottomCenter)
                    .padding(top = 64.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 12.dp),
                    text = ingredient.name,
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_expire),
                        contentDescription = null,
                        tint = MainText
                    )

                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = stringResource(
                            id = R.string.ingredient_item_exp_date,
                            ingredient.expireDate.date,
                            ingredient.expireDate.month,
                            ingredient.expireDate.year
                        ),
                        textAlign = TextAlign.Left,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = MainText
                    )
                }
            }

            if (showDeleteButton) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .size(40.dp),
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(8.dp),
                    containerColor = MainAccent,
                    contentColor = Color.White

                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null )
                }
            }


        }
    }
}