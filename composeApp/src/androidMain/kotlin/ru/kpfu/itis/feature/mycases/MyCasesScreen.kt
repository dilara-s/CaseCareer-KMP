package ru.kpfu.itis.feature.mycases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.koin.compose.viewmodel.koinViewModel
import ru.kpfu.itis.R
import ru.kpfu.itis.designSystem.Primary
import ru.kpfu.itis.feature.mycases.domain.model.MyCase
import ru.kpfu.itis.feature.mycases.presentation.MyCasesEvent
import ru.kpfu.itis.feature.mycases.presentation.MyCasesState
import ru.kpfu.itis.feature.mycases.presentation.MyCasesViewModel

@Composable
fun MyCasesRoute(
    viewModel: MyCasesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Firebase.analytics.logEvent("launch_my_cases", null)
    }

    MyCasesScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun MyCasesScreen(
    state: MyCasesState,
    onEvent: (MyCasesEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (state.cases.isEmpty())
                    stringResource(R.string.my_cases_title)
                else
                    stringResource(R.string.my_cases_title_count, state.cases.size),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.my_cases_load_error),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.error!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { onEvent(MyCasesEvent.Refresh) },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text(stringResource(R.string.common_retry), color = Color.White)
                    }
                }
            }
            state.cases.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.my_cases_empty_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.my_cases_empty_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.cases, key = { it.caseId }) { case ->
                        MyCaseCard(case = case)
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
private fun MyCaseCard(case: MyCase) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatusBadge(status = case.status)

            Spacer(Modifier.height(10.dp))

            Text(
                text = case.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = case.companyName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.my_cases_submitted_at, formatSubmittedDate(case.submittedAt)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val label = when (status) {
        "SENT" -> stringResource(R.string.status_sent)
        "REVIEW" -> stringResource(R.string.status_review)
        "ACCEPTED" -> stringResource(R.string.status_accepted)
        "REJECTED" -> stringResource(R.string.status_rejected)
        else -> status
    }
    val bgColor = when (status) {
        "SENT" -> Color(0xFFE8F0FE)
        "REVIEW" -> Color(0xFFFFF3CD)
        "ACCEPTED" -> Color(0xFFE6F4EA)
        "REJECTED" -> Color(0xFFFDE8E8)
        else -> Color(0xFFF0F0F0)
    }
    val textColor = when (status) {
        "SENT" -> Color(0xFF3B5BDB)
        "REVIEW" -> Color(0xFF92650A)
        "ACCEPTED" -> Color(0xFF1E7E34)
        "REJECTED" -> Color(0xFFB91C1C)
        else -> Color(0xFF6B7280)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatSubmittedDate(submittedAt: String): String {
    return try {
        val datePart = submittedAt.substringBefore('T')
        val parts = datePart.split('-')
        val day = parts[2].trimStart('0')
        val month = when (parts[1]) {
            "01" -> "января"; "02" -> "февраля"; "03" -> "марта"
            "04" -> "апреля"; "05" -> "мая"; "06" -> "июня"
            "07" -> "июля"; "08" -> "августа"; "09" -> "сентября"
            "10" -> "октября"; "11" -> "ноября"; "12" -> "декабря"
            else -> ""
        }
        "$day $month"
    } catch (e: Exception) { submittedAt }
}
