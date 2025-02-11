package com.rizwansayyed.zene.ui.main.search.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun SearchBarView(
    search: MutableState<String>, showSearch: String, onSearch: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(10.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        TextField(
            TextFieldValue(
                text = search.value, selection = TextRange(search.value.length)
            ),
            {
                if (it.text.length <= 700) search.value = it.text
                if (it.text.trim().isEmpty()) onSearch("")
            },
            Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions {
                if (search.value.length > 3) onSearch(search.value)
            },
            placeholder = {
                TextViewNormal(stringResource(R.string.search_s_p_a_etc), 14)
            },
            trailingIcon = {
                if (search.value.length > 3) {
                    IconButton({
                        onSearch(search.value)
                    }) {
                        ImageIcon(R.drawable.ic_search, 24)
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MainColor,
                unfocusedContainerColor = MainColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White
            ),
            singleLine = true
        )

        if (showSearch.length > 3) Box(Modifier
            .padding(start = 5.dp)
            .clickable {
                search.value = ""
                onSearch("")
            }) {
            ImageIcon(R.drawable.ic_link_backward, 24)
        }
    }
}