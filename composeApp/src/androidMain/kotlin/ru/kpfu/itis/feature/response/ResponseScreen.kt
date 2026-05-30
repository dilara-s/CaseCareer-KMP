package ru.kpfu.itis.feature.response

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import ru.kpfu.itis.R
import ru.kpfu.itis.designSystem.Primary
import ru.kpfu.itis.feature.response.presentation.*

@Composable
fun ResponseRoute(
    caseId: Int,
    caseTitle: String,
    companyName: String,
    ndaRequired: Boolean,
    navController: NavHostController,
    viewModel: ResponseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ResponseEvent.Init(caseId, caseTitle, companyName, ndaRequired))
        viewModel.effect.collect { effect ->
            when (effect) {
                ResponseEffect.CloseSheet -> navController.popBackStack()
                ResponseEffect.NavigateToMyCases -> {
                    navController.navigate("my_cases") {
                        popUpTo("feed") { inclusive = false }
                    }
                }
            }
        }
    }

    when (state.screenMode) {
        ResponseScreenMode.NdaStep -> NdaStepScreen(state, viewModel::onEvent)
        ResponseScreenMode.FormStep -> FormStepScreen(state, viewModel::onEvent)
        ResponseScreenMode.SuccessStep -> SuccessStepScreen(state, viewModel::onEvent)
    }
}

@Composable
private fun StepProgressBar(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (index < currentStep) Primary else Color(0xFFE5E7EB))
            )
        }
    }
}

@Composable
private fun NdaStepScreen(state: ResponseState, onEvent: (ResponseEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onEvent(ResponseEvent.Cancel) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.case_detail_back_cd),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = stringResource(R.string.response_nda_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        StepProgressBar(currentStep = 1, totalSteps = state.totalSteps)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFF3CD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = Color(0xFF92650A),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.response_nda_heading),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.response_nda_description),
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(R.string.response_nda_link),
                    color = Primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(20.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.response_nda_client_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.companyName.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = state.companyName,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.response_nda_executor_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(R.string.response_nda_executor_me),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            NdaCheckItem(
                text = stringResource(R.string.response_nda_check1),
                checked = state.isNdaAccepted,
                onToggle = { onEvent(ResponseEvent.ToggleNdaAccepted) }
            )

            Spacer(Modifier.height(8.dp))

            NdaCheckItem(
                text = stringResource(R.string.response_nda_check2),
                checked = state.isNdaAccepted,
                onToggle = { onEvent(ResponseEvent.ToggleNdaAccepted) }
            )

            if (state.ndaError != null) {
                Text(
                    text = state.ndaError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Button(
                onClick = { onEvent(ResponseEvent.ProceedFromNda) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
            ) {
                Text(stringResource(R.string.response_nda_sign_button), fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = { onEvent(ResponseEvent.Cancel) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.common_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun NdaCheckItem(text: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = Primary),
            modifier = Modifier.padding(top = 2.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun FormStepScreen(state: ResponseState, onEvent: (ResponseEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onEvent(ResponseEvent.Cancel) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.case_detail_back_cd),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = stringResource(R.string.response_form_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        StepProgressBar(
            currentStep = if (state.isNdaRequired) 2 else 1,
            totalSteps = state.totalSteps
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.response_cover_letter_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = state.coverLetter,
                onValueChange = { onEvent(ResponseEvent.UpdateCoverLetter(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.response_cover_letter_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                },
                isError = state.coverLetterError != null,
                supportingText = state.coverLetterError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.response_solution_link_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = state.solutionLink,
                onValueChange = { onEvent(ResponseEvent.UpdateSolutionLink(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        stringResource(R.string.response_solution_link_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                isError = state.solutionLinkError != null,
                supportingText = state.solutionLinkError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Button(
                onClick = { onEvent(ResponseEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.response_submit_button), fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = { onEvent(ResponseEvent.Cancel) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.common_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun SuccessStepScreen(state: ResponseState, onEvent: (ResponseEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(48.dp))
            Text(
                text = stringResource(R.string.response_form_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        StepProgressBar(currentStep = state.totalSteps, totalSteps = state.totalSteps)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color(0xFF34C759)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✓", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.response_success_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.response_success_description, state.companyName),
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StatusTimelineItem(
                        dotColor = Primary,
                        title = stringResource(R.string.response_success_sent_label),
                        subtitle = formatSubmittedAt(state.submittedAt)
                    )
                    Spacer(Modifier.height(12.dp))
                    val statusLabel = when (state.responseStatus) {
                        "SENT" -> stringResource(R.string.status_sent)
                        "REVIEW" -> stringResource(R.string.status_review)
                        "ACCEPTED" -> stringResource(R.string.status_accepted)
                        "REJECTED" -> stringResource(R.string.status_rejected)
                        else -> state.responseStatus ?: ""
                    }
                    StatusTimelineItem(
                        dotColor = Primary,
                        title = stringResource(R.string.response_success_status_format, statusLabel),
                        subtitle = stringResource(R.string.response_success_reviewing)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        Button(
            onClick = { onEvent(ResponseEvent.BackToFeed) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
        ) {
            Text(stringResource(R.string.response_back_to_feed_button), fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun StatusTimelineItem(dotColor: Color, title: String, subtitle: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(dotColor)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatSubmittedAt(submittedAt: String?): String {
    if (submittedAt == null) return ""
    return try {
        val datePart = submittedAt.substringBefore('T')
        val timePart = submittedAt.substringAfter('T').substringBefore('.')
        val parts = datePart.split('-')
        val day = parts[2].trimStart('0')
        val month = when (parts[1]) {
            "01" -> "января"; "02" -> "февраля"; "03" -> "марта"
            "04" -> "апреля"; "05" -> "мая"; "06" -> "июня"
            "07" -> "июля"; "08" -> "августа"; "09" -> "сентября"
            "10" -> "октября"; "11" -> "ноября"; "12" -> "декабря"
            else -> ""
        }
        "$day $month, $timePart"
    } catch (e: Exception) { submittedAt }
}
