package ru.kpfu.itis.feature.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.kpfu.itis.R
import ru.kpfu.itis.designSystem.Primary
import ru.kpfu.itis.feature.feed.domain.model.CaseDetail
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailEffect
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailEvent
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailState
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailViewModel

@Composable
fun CaseDetailRoute(
    caseId: Long,
    onBack: () -> Unit,
    onApplyNda: (Long) -> Unit,
    onApplyForm: (Long) -> Unit,
    viewModel: CaseDetailViewModel = koinViewModel(parameters = { parametersOf(caseId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CaseDetailEffect.NavigateBack -> onBack()
                is CaseDetailEffect.NavigateToNdaStep -> onApplyNda(effect.caseId)
                is CaseDetailEffect.NavigateToApplyStep -> onApplyForm(effect.caseId)
                is CaseDetailEffect.ShowError -> { /* handled via state.error */ }
            }
        }
    }

    CaseDetailScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun CaseDetailScreen(
    state: CaseDetailState,
    onEvent: (CaseDetailEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onEvent(CaseDetailEvent.Back) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            state.error != null && state.case == null -> {
                DetailErrorState(
                    message = state.error!!,
                    onRetry = { onEvent(CaseDetailEvent.Retry) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            state.case != null -> {
                CaseDetailContent(
                    case = state.case!!,
                    onApply = { onEvent(CaseDetailEvent.Apply) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CaseDetailContent(
    case: CaseDetail,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = case.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 27.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = formatDetailReward(case.reward),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Дедлайн до ${formatDetailDeadline(case.deadline)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Text(
                    text = if (case.ndaRequired) "NDA требуется" else "NDA не требуется",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Primary, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = case.companyName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = case.companyName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Опубликовано ${formatDetailDate(case.publishedAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Описание",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = case.description,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(24.dp))
        }

        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Откликнуться",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun DetailErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Не удалось загрузить кейс",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Повторить", color = Color.White)
        }
    }
}

private fun formatDetailReward(reward: String): String {
    return try {
        val amount = reward.toDouble().toLong()
        val formatted = buildString {
            val str = amount.toString()
            str.reversed().forEachIndexed { i, c ->
                if (i > 0 && i % 3 == 0) append(' ')
                append(c)
            }
        }.reversed()
        "$formatted ₽"
    } catch (e: Exception) {
        "$reward ₽"
    }
}

private fun formatDetailDeadline(deadline: String): String {
    return try {
        val datePart = deadline.substringBefore('T')
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
    } catch (e: Exception) { deadline }
}

private fun formatDetailDate(date: String): String {
    return try {
        val datePart = date.substringBefore('T')
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
    } catch (e: Exception) { date }
}
