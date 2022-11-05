package com.sachin.app.smsdetection.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sachin.app.smsdetection.MainViewModel
import com.sachin.app.smsdetection.data.category.ExpenseCategory
import com.sachin.app.smsdetection.data.category.TransactionType
import com.sachin.app.smsdetection.data.model.LocalSms
import com.sachin.app.smsdetection.data.model.TransactionDetail
import com.sachin.app.smsdetection.screen.destinations.SmsDetailScreenDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun TransactionListScreen(
    viewModel: MainViewModel,
    navigator: DestinationsNavigator
) {
    val smsList = viewModel.smsList
    val isDropDownExpanded = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.heightIn(48.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.selectedCategory?.name ?: "All categories",
                            modifier = Modifier.clickable {
                                isDropDownExpanded.value = !isDropDownExpanded.value
                            }
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = "Total: " + smsList.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (smsList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No SMS found")
                }
            } else {
                LazyColumn {
                    items(smsList) { item: TransactionDetail ->
                        TransactionListItem(
                            transaction = item,
                            onClick = {
                                navigator.navigate(
                                    SmsDetailScreenDestination(item.sms)
                                )
                            }
                        )
                    }
                }
            }
        }

        CategoryDropDown(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            },
            onMenuItemClick = {
                viewModel.selectedCategory = it
                isDropDownExpanded.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionListItem(
    transaction: TransactionDetail,
    onClick: (LocalSms) -> Unit
) {
    ListItem(
        text = {
            Text(text = transaction.title.toString())
        },
        secondaryText = {
            Text(text = "${transaction.category} | ${transaction.date}")
        },
        trailing = {
            Text(
                text = when (transaction.transactionType) {
                    TransactionType.Debit -> "- "
                    TransactionType.Credit -> "+ "
                    TransactionType.Unknown -> ""
                } + transaction.amount
            )
        },
        overlineText = {
            Text(
                text = when (transaction) {
                    is TransactionDetail.Bank -> "Account no: " + transaction.accountNumber
                    is TransactionDetail.Card -> "Card no: " + transaction.cardNumber
                }
            )
        },
        modifier = Modifier.clickable {
            onClick(transaction.sms)
        }
    )
}

@Composable
fun CategoryDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onMenuItemClick: (ExpenseCategory?) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(16.dp, 0.dp)
    ) {
        DropdownMenuItem(onClick = {
            onMenuItemClick(null)
        }) {
            Text(text = "All categories")
        }
        for (category in ExpenseCategory.values()) {
            DropdownMenuItem(onClick = {
                onMenuItemClick(category)
            }) {
                Text(text = category.name)
            }
        }
    }
}