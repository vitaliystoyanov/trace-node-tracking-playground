package io.architecture.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun showMessage(msg: String) {
    val openAlertDialog = remember { mutableStateOf(true) }
    when {
        openAlertDialog.value -> AlertDebugDialog(
            dialogText = msg,
            onConfirmation = {
                openAlertDialog.value = false
            }
        )
    }
}

@Composable
fun AlertDebugDialog(
    onConfirmation: () -> Unit,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = "Debug message")
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        }
    )
}