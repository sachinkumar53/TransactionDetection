package com.sachin.app.smsdetection.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sachin.app.smsdetection.data.model.LocalSms
import java.text.SimpleDateFormat
import java.util.*

@Destination
@Composable
fun SmsDetailScreen(
    sms: LocalSms,
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = sms.sender)
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SelectionContainer {
                Text(text = sms.text)
            }
            Text(
                text = SimpleDateFormat("HH:mm dd MMM yy", Locale.getDefault()).format(sms.date),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}