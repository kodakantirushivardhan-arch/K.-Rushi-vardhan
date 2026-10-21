package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.SparkCrimson
import com.example.ui.theme.SparkRuby

@Composable
fun ReportBlockDialog(
    user: UserProfile,
    onDismiss: () -> Unit,
    onReport: (reason: String) -> Unit,
    onBlock: () -> Unit
) {
    var selectedReason by remember { mutableStateOf("Inappropriate messages or content") }
    var isReporting by remember { mutableStateOf(false) }

    val reportReasons = listOf(
        "Inappropriate messages or content",
        "Fake profile or catfishing",
        "Harassment or rude behavior",
        "Commercial spam or advertising",
        "Underage account"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isReporting) "Report ${user.name}" else "Safety Options for ${user.name}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            if (isReporting) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Why are you reporting this profile? We will investigate promptly.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    reportReasons.forEach { reason ->
                        val isSelected = selectedReason == reason
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedReason = reason }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedReason = reason },
                                colors = RadioButtonDefaults.colors(selectedColor = SparkCrimson)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = reason,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Would you like to block or report this user? Blocked accounts can never see you or message you again.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isReporting = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SparkCrimson),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("report_account_option")
                    ) {
                        Text("Report Account", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onBlock,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SparkRuby),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("block_account_option")
                    ) {
                        Text("Block Account", color = SparkRuby, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            if (isReporting) {
                Button(
                    onClick = { onReport(selectedReason) },
                    colors = ButtonDefaults.buttonColors(containerColor = SparkCrimson),
                    modifier = Modifier.testTag("submit_report_button")
                ) {
                    Text("Submit Report", color = Color.White)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_report_button")
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
