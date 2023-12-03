package com.rizwansayyed.zene.service.implementation.recentplay

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface RecentPlayingSongInterface {
    suspend fun updateRecentPlayingSongInfo(): Flow<String>
    suspend fun updateLatestListenTiming(): Flow<String>
}