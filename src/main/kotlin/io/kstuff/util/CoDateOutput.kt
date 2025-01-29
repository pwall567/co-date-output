/*
 * @(#) CoDateOutput.java
 *
 * co-date-output  Non-blocking date output functions
 * Copyright (c) 2022 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.kstuff.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Year
import java.time.YearMonth
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date

import io.kstuff.util.CoIntOutput.output2Digits
import io.kstuff.util.CoIntOutput.output3Digits

/**
 * Non-blocking functions used in the output of date and time values in RFC 3339 string representations.  The functions
 * all output digits left-to-right, avoiding the need to allocate separate objects to hold intermediate strings.
 *
 * Each of the functions comes in two forms &ndash; one that takes a `suspend` function as its last parameter, and an
 * extension function on [CoOutput].
 *
 * @author  Peter Wall
 */
object CoDateOutput {

    private const val SECONDS_PER_DAY = 24 * 60 * 60
    private const val SECONDS_PER_HOUR = 60 * 60
    private const val SECONDS_PER_MINUTE = 60
    private const val MILLIS_PER_DAY = SECONDS_PER_DAY * 1000
    private const val MILLIS_PER_HOUR = SECONDS_PER_HOUR * 1000
    private const val MILLIS_PER_MINUTE = SECONDS_PER_MINUTE * 1000
    private const val MILLIS_PER_SECOND = 1000
    private const val NANOS_PER_MILLI = 1_000_000

    suspend fun coOutputDate(date: Date, out: CoOutput) = out.outputDate(date)

    suspend fun CoOutput.outputDate(date: Date) {
        val epochMillis = date.time
        val days = Math.floorDiv(epochMillis, MILLIS_PER_DAY.toLong())
        outputLocalDate(LocalDate.ofEpochDay(days))
        output('T')
        var millis = (epochMillis - (days * MILLIS_PER_DAY)).toInt()
        val hours = millis / MILLIS_PER_HOUR
        millis -= hours * MILLIS_PER_HOUR
        val minutes = millis / MILLIS_PER_MINUTE
        millis -= minutes * MILLIS_PER_MINUTE
        val seconds = millis / MILLIS_PER_SECOND
        millis -= seconds * MILLIS_PER_SECOND
        outputTime(hours, minutes, seconds, millis * NANOS_PER_MILLI)
        output('Z')
    }

    suspend fun coOutputCalendar(calendar: Calendar, out: CoOutput) = out.outputCalendar(calendar)

    suspend fun CoOutput.outputCalendar(calendar: Calendar) {
        outputYearValue(calendar.get(Calendar.YEAR))
        output('-')
        output2Digits(calendar.get(Calendar.MONTH) + 1)
        output('-')
        output2Digits(calendar.get(Calendar.DAY_OF_MONTH))
        output('T')
        outputTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND) * NANOS_PER_MILLI)
        val offsetMillis = calendar.get(Calendar.ZONE_OFFSET) +
                if (calendar.timeZone.inDaylightTime(calendar.time)) calendar.get(Calendar.DST_OFFSET) else 0
        outputOffset(offsetMillis / 1000)
    }

    suspend fun coOutputInstant(instant: Instant, out: CoOutput) = out.outputInstant(instant)

    suspend fun CoOutput.outputInstant(instant: Instant) {
        val epochSeconds = instant.epochSecond
        val days = Math.floorDiv(epochSeconds, SECONDS_PER_DAY.toLong())
        outputLocalDate(LocalDate.ofEpochDay(days))
        output('T')
        val seconds = (epochSeconds - days * SECONDS_PER_DAY).toInt()
        val hours = seconds / SECONDS_PER_HOUR
        val remainder = seconds - hours * SECONDS_PER_HOUR
        val minutes = remainder / SECONDS_PER_MINUTE
        outputTime(hours, minutes, remainder - minutes * SECONDS_PER_MINUTE, instant.nano)
        output('Z')
    }

    suspend fun coOutputZonedDateTime(zonedDateTime: ZonedDateTime, out: CoOutput) =
            out.outputZonedDateTime(zonedDateTime)

    suspend fun CoOutput.outputZonedDateTime(zonedDateTime: ZonedDateTime) {
        outputLocalDateTime(zonedDateTime.toLocalDateTime())
        outputOffset((zonedDateTime.offset.totalSeconds))
    }

    suspend fun coOutputOffsetDateTime(offsetDateTime: OffsetDateTime, out: CoOutput) =
            out.outputOffsetDateTime(offsetDateTime)

    suspend fun CoOutput.outputOffsetDateTime(offsetDateTime: OffsetDateTime) {
        outputLocalDateTime(offsetDateTime.toLocalDateTime())
        outputOffset(offsetDateTime.offset.totalSeconds)
    }

    suspend fun coOutputOffsetTime(offsetTime: OffsetTime, out: CoOutput) = out.outputOffsetTime(offsetTime)

    suspend fun CoOutput.outputOffsetTime(offsetTime: OffsetTime) {
        outputLocalTime(offsetTime.toLocalTime())
        outputOffset(offsetTime.offset.totalSeconds)
    }

    suspend fun coOutputLocalDateTime(localDateTime: LocalDateTime, out: CoOutput) =
            out.outputLocalDateTime(localDateTime)

    suspend fun CoOutput.outputLocalDateTime(localDateTime: LocalDateTime) {
        outputLocalDate(localDateTime.toLocalDate())
        output('T')
        outputLocalTime(localDateTime.toLocalTime())
    }

    suspend fun coOutputLocalDate(localDate: LocalDate, out: CoOutput) = out.outputLocalDate(localDate)

    suspend fun CoOutput.outputLocalDate(localDate: LocalDate) {
        outputYearValue(localDate.year)
        output('-')
        output2Digits(localDate.monthValue)
        output('-')
        output2Digits(localDate.dayOfMonth)
    }

    suspend fun coOutputLocalTime(localTime: LocalTime, out: CoOutput) = out.outputLocalTime(localTime)

    suspend fun CoOutput.outputLocalTime(localTime: LocalTime) {
        outputTime(localTime.hour, localTime.minute, localTime.second, localTime.nano)
    }

    suspend fun coOutputYear(year: Year, out: CoOutput) = out.outputYear(year)

    suspend fun CoOutput.outputYear(year: Year) {
        outputYearValue(year.value)
    }

    suspend fun coOutputYearMonth(yearMonth: YearMonth, out: CoOutput) = out.outputYearMonth(yearMonth)

    suspend fun CoOutput.outputYearMonth(yearMonth: YearMonth) {
        outputYearValue(yearMonth.year)
        output('-')
        output2Digits(yearMonth.monthValue)
    }

    suspend fun coOutputMonthDay(monthDay: MonthDay, out: CoOutput) = out.outputMonthDay(monthDay)

    suspend fun CoOutput.outputMonthDay(monthDay: MonthDay) {
        output('-')
        output('-')
        output2Digits(monthDay.monthValue)
        output('-')
        output2Digits(monthDay.dayOfMonth)
    }

    private suspend fun CoOutput.outputYearValue(year: Int) {
        val value = (if (year > 0) year else -year + 1).coerceAtMost(9999)
        val century = value / 100
        output2Digits(century)
        output2Digits(value - century * 100)
    }

    private suspend fun CoOutput.outputTime(hours: Int, minutes: Int, seconds: Int, nanos: Int) {
        output2Digits(hours)
        output(':')
        output2Digits(minutes)
        output(':')
        output2Digits(seconds)
        if (nanos > 0) {
            output('.')
            var digits = nanos / 1_000_000
            output3Digits(digits)
            val last6 = nanos - digits * 1_000_000
            if (last6 > 0) {
                digits = last6 / 1_000
                output3Digits(digits)
                val last3 = last6 - digits * 1000
                if (last3 > 0)
                    output3Digits(last3)
            }
        }
    }

    private suspend fun CoOutput.outputOffset(seconds: Int) {
        if (seconds == 0)
            output('Z')
        else {
            val s = if (seconds < 0) {
                output('-')
                -seconds
            } else {
                output('+')
                seconds
            }
            val hours = s / SECONDS_PER_HOUR
            val minutes = (s - hours * SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
            output2Digits(hours)
            output(':')
            output2Digits(minutes)
        }
    }

}
