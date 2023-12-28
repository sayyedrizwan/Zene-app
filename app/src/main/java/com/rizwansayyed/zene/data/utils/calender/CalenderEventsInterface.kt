package com.rizwansayyed.zene.data.utils.calender

import com.rizwansayyed.zene.domain.CalendarEvents
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CalenderEventsInterface {


    suspend fun todayCalenderEvent(): Flow<MutableList<CalendarEvents>>
}