package com.mready.myapplication.ui.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText


@Composable
fun FridgeBuddyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        fontFamily = Poppins,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = MainText
    ),
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    label: @Composable (() -> Unit)? = null,
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MainAccent,
        backgroundColor = LightAccent
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            maxLines = maxLines,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            label = label,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(.6f)
                        .padding(top = 4.dp),
                    text = placeholder ?: "",
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryText
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                errorContainerColor = Background,
                focusedIndicatorColor = MainAccent,
                unfocusedIndicatorColor = LightAccent,
                focusedLabelColor = MainAccent,
                unfocusedLabelColor = LightAccent,
                errorLabelColor = Error,
                errorIndicatorColor = Error,
                cursorColor = MainText,
                disabledContainerColor = Background,
                disabledIndicatorColor = MainAccent,
            ),
            isError = isError,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            supportingText = supportingText,
        )
    }
}