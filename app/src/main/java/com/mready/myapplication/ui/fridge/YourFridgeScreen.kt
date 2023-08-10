package com.mready.myapplication.ui.fridge

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.models.mockedIngredients
import com.mready.myapplication.ui.dashboard.IngredientItem
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText

@Composable
fun YourFridgeScreen(
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 20.dp),
                text = stringResource(id = R.string.fridge_title),
                fontSize = 28.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.fridge_soon_to_expire),
                    textAlign = TextAlign.Left,
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryText
                )

                Row(
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

//            FridgeSoonToExpire()

            LazyRow(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                items(mockedIngredients) {
                    IngredientItem(
                        modifier = Modifier.width(120.dp),
                        ingredient = it
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.fridge_all_ingredients),
                    textAlign = TextAlign.Left,
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryText
                )

                //todo edit here???

            }

            LazyVerticalGrid(
                modifier = Modifier.padding(top = 20.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp)
            ) {
                items(mockedIngredients) {
                    IngredientItem(
                        modifier = Modifier.width(120.dp),
                        ingredient = it,
                        showEditButton = false
                    )
                }
            }

        }

        FloatingActionButton(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .align(Alignment.BottomCenter),
            onClick = onAddClick,
            containerColor = MainAccent,
            contentColor = Background,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.fridge_add),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }


}