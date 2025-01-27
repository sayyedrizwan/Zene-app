package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun ConnectProfileMessageButton(user: ConnectUserResponse?) {
    var messageText by remember { mutableStateOf("") }

    TextViewBold(stringResource(R.string.chats), 19, Color.White)
    Spacer(Modifier.height(6.dp))

    TextViewNormal(
        "${user?.name}: sbhsbshsbs dhdddhd duhduhdd dhdudhd dudhduduhdhdhdud ch  cggycgd duhddhddydydgd duhdudhudhduhdud dhudhdyduududuhcbhc cudygddgdygdygd",
        14,
        Color.White
    )

    Spacer(Modifier.height(6.dp))

    TextField(
        messageText, {
            if (it.length <= 140) {
                messageText = it
            }
        },
        Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        placeholder = {
            TextViewNormal(
                stringResource(R.string.enter_your_message), 14, color = Color.Black
            )
        },
        trailingIcon = {
            if (messageText.length > 3) {
                IconButton({ }) {
                    ImageIcon(R.drawable.ic_sent, 24, Color.Black)
                }
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedTextColor = Color.Black,
            disabledTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        singleLine = true
    )

    Spacer(Modifier.height(80.dp))
}