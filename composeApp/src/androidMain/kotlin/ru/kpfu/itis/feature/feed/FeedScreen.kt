package ru.kpfu.itis.feature.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.viewmodel.koinViewModel
import ru.kpfu.itis.R
import ru.kpfu.itis.designSystem.Primary
import ru.kpfu.itis.designSystem.Theme.LocalExtendedColors
import ru.kpfu.itis.feature.feed.domain.model.Case
import ru.kpfu.itis.feature.feed.presentation.feed.FeedEffect
import ru.kpfu.itis.feature.feed.presentation.feed.FeedEvent
import ru.kpfu.itis.feature.feed.presentation.feed.FeedState
import ru.kpfu.itis.feature.feed.presentation.feed.FeedViewModel

@Composable
fun FeedRoute(
    onCaseClick: (Long) -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Firebase.analytics.logEvent("launch_feed", null)
        viewModel.effect.collect { effect ->
            when (effect) {
                is FeedEffect.NavigateToCaseDetail -> onCaseClick(effect.caseId)
                is FeedEffect.ShowError -> { /* можно добавить Snackbar */ }
            }
        }
    }

    FeedScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun FeedScreen(
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { onEvent(FeedEvent.SearchQueryChanged(it)) },
            onClear = { onEvent(FeedEvent.ClearSearch) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        )

        if (state.searchQuery.isNotBlank()) {
            Text(
                text = stringResource(R.string.feed_results_count, state.totalCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary,
                    )
                }
                state.error != null && state.cases.isEmpty() -> {
                    ErrorState(
                        message = state.error!!,
                        onRetry = { onEvent(FeedEvent.Refresh) },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    CaseList(
                        cases = state.cases,
                        isLoadingMore = state.isLoadingMore,
                        hasMore = state.hasMore,
                        onCaseClick = { onEvent(FeedEvent.CaseClicked(it)) },
                        onLoadMore = { onEvent(FeedEvent.LoadNextPage) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(R.string.feed_search_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle =
                        TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp,
                        ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.feed_search_clear_cd),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CaseList(
    cases: List<Case>,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onCaseClick: (Long) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= cases.size - 5 && hasMore && !isLoadingMore) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(cases, key = { it.id }) { case ->
            CaseCard(
                case = case,
                onApplyClick = { onCaseClick(case.id) },
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Primary, strokeWidth = 2.dp)
                }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun CaseCard(
    case: Case,
    onApplyClick: () -> Unit,
) {
    val extColors = LocalExtendedColors.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            NdaBadge(
                ndaRequired = case.ndaRequired,
                bgColor = if (case.ndaRequired) extColors.ndaRequiredBg else extColors.ndaNotRequiredBg,
                textColor = if (case.ndaRequired) extColors.ndaRequiredText else extColors.ndaNotRequiredText,
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = case.title,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 21.sp,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = formatReward(case.reward),
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = case.companyName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = stringResource(R.string.feed_deadline_prefix, formatDeadline(case.deadline)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onApplyClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.feed_apply_button),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }
        }
    }
}

@Composable
private fun NdaBadge(
    ndaRequired: Boolean,
    bgColor: Color,
    textColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                painter =
                    painterResource(
                        if (ndaRequired) R.drawable.ic_lock else R.drawable.ic_lock_open,
                    ),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = stringResource(if (ndaRequired) R.string.common_nda_required else R.string.common_nda_not_required),
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.feed_load_error),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
        ) {
            Text(stringResource(R.string.common_retry), color = Color.White)
        }
    }
}

private fun formatReward(reward: String): String {
    return try {
        val amount = reward.toDouble().toLong()
        val formatted =
            buildString {
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

private fun formatDeadline(deadline: String): String {
    return try {
        val datePart = deadline.substringBefore('T')
        val parts = datePart.split('-')
        val day = parts[2].trimStart('0')
        val month =
            when (parts[1]) {
                "01" -> "января"
                "02" -> "февраля"
                "03" -> "марта"
                "04" -> "апреля"
                "05" -> "мая"
                "06" -> "июня"
                "07" -> "июля"
                "08" -> "августа"
                "09" -> "сентября"
                "10" -> "октября"
                "11" -> "ноября"
                "12" -> "декабря"
                else -> ""
            }
        "$day $month"
    } catch (e: Exception) {
        deadline
    }
}
