/*
 * @(#) CoOutputDateTest.java
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

package net.pwall.util

import kotlin.test.Test
import kotlinx.coroutines.runBlocking

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date

import io.kstuff.test.shouldBe

import net.pwall.util.CoDateOutput.coOutputCalendar
import net.pwall.util.CoDateOutput.coOutputDate
import net.pwall.util.CoDateOutput.coOutputInstant
import net.pwall.util.CoDateOutput.coOutputLocalDate
import net.pwall.util.CoDateOutput.coOutputLocalDateTime
import net.pwall.util.CoDateOutput.coOutputLocalTime
import net.pwall.util.CoDateOutput.coOutputMonthDay
import net.pwall.util.CoDateOutput.coOutputOffsetDateTime
import net.pwall.util.CoDateOutput.coOutputOffsetTime
import net.pwall.util.CoDateOutput.coOutputYear
import net.pwall.util.CoDateOutput.coOutputYearMonth
import net.pwall.util.CoDateOutput.coOutputZonedDateTime
import net.pwall.util.CoDateOutput.outputCalendar
import net.pwall.util.CoDateOutput.outputDate
import net.pwall.util.CoDateOutput.outputInstant
import net.pwall.util.CoDateOutput.outputLocalDate
import net.pwall.util.CoDateOutput.outputLocalDateTime
import net.pwall.util.CoDateOutput.outputLocalTime
import net.pwall.util.CoDateOutput.outputMonthDay
import net.pwall.util.CoDateOutput.outputOffsetDateTime
import net.pwall.util.CoDateOutput.outputOffsetTime
import net.pwall.util.CoDateOutput.outputYear
import net.pwall.util.CoDateOutput.outputYearMonth
import net.pwall.util.CoDateOutput.outputZonedDateTime

class CoOutputDateTest {

    @Test fun `should output Date using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        val instant = OffsetDateTime.of(2022, 4, 8, 18, 39, 2, 456_000_000, ZoneOffset.UTC).toInstant()
        coOutputDate(Date.from(instant)) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "2022-04-08T18:39:02.456Z"
    }

    @Test fun `should output Date using extension function`() = runBlocking {
        val capture = CoCapture()
        val instant = OffsetDateTime.of(2022, 4, 8, 18, 39, 2, 456_000_000, ZoneOffset.UTC).toInstant()
        capture.outputDate(Date.from(instant))
        capture.toString() shouldBe "2022-04-08T18:39:02.456Z"
    }

    @Test fun `should output Calendar using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2022)
        calendar.set(Calendar.MONTH, 3)
        calendar.set(Calendar.DAY_OF_MONTH, 8)
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 39)
        calendar.set(Calendar.SECOND, 2)
        calendar.set(Calendar.MILLISECOND, 456)
        calendar.set(Calendar.ZONE_OFFSET, 10 * 60 * 60 * 1000)
        coOutputCalendar(calendar) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "2022-04-08T18:39:02.456+10:00"
    }

    @Test fun `should output Calendar using extension function`() = runBlocking {
        val capture = CoCapture()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2022)
        calendar.set(Calendar.MONTH, 3)
        calendar.set(Calendar.DAY_OF_MONTH, 8)
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 39)
        calendar.set(Calendar.SECOND, 2)
        calendar.set(Calendar.MILLISECOND, 456)
        calendar.set(Calendar.ZONE_OFFSET, 10 * 60 * 60 * 1000)
        capture.outputCalendar(calendar)
        capture.toString() shouldBe "2022-04-08T18:39:02.456+10:00"
    }

    @Test fun `should output Instant using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        val localDateTime = LocalDateTime.parse("2022-04-07T18:32:47.12")
        val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(10))
        val instant = offsetDateTime.toInstant()
        coOutputInstant(instant) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "2022-04-07T08:32:47.120Z"
    }

    @Test fun `should output Instant using extension function`() = runBlocking {
        val capture = CoCapture()
        val localDateTime = LocalDateTime.parse("2022-04-07T18:32:47.12")
        val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(10))
        val instant = offsetDateTime.toInstant()
        capture.outputInstant(instant)
        capture.toString() shouldBe "2022-04-07T08:32:47.120Z"
    }

    @Test fun `should output ZonedDateTime using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        val localDateTime = LocalDateTime.parse("2022-04-07T18:32:47.55")
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Australia/Sydney"))
        coOutputZonedDateTime(zonedDateTime) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "2022-04-07T18:32:47.550+10:00"
    }

    @Test fun `should output ZonedDateTime using extension function`() = runBlocking {
        val capture = CoCapture()
        val localDateTime = LocalDateTime.parse("2022-04-07T18:32:47.55")
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Australia/Sydney"))
        capture.outputZonedDateTime(zonedDateTime)
        capture.toString() shouldBe "2022-04-07T18:32:47.550+10:00"
    }

    @Test fun `should output OffsetDateTime using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        LocalDateTime.parse("2022-04-07T18:32:47.5446").let { localDateTime ->
            val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(10))
            coOutputOffsetDateTime(offsetDateTime) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "2022-04-07T18:32:47.544600+10:00"
        }
        i = 0
        LocalDateTime.of(1999, 6, 1, 9, 15, 10, 456_000_000).let { localDateTime ->
            val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
            coOutputOffsetDateTime(offsetDateTime) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "1999-06-01T09:15:10.456Z"
        }
    }

    @Test fun `should output OffsetDateTime using extension function`() = runBlocking {
        val capture = CoCapture()
        LocalDateTime.parse("2022-04-07T18:32:47.5446").let { localDateTime ->
            val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(10))
            capture.outputOffsetDateTime(offsetDateTime)
            capture.toString() shouldBe "2022-04-07T18:32:47.544600+10:00"
        }
        capture.reset()
        LocalDateTime.of(1999, 6, 1, 9, 15, 10, 456_000_000).let { localDateTime ->
            val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
            capture.outputOffsetDateTime(offsetDateTime)
            capture.toString() shouldBe "1999-06-01T09:15:10.456Z"
        }
    }

    @Test fun `should output OffsetTime using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        val offsetTime = OffsetTime.of(8, 27, 55, 544_233_100, ZoneOffset.ofHours(-5))
        coOutputOffsetTime(offsetTime) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "08:27:55.544233100-05:00"
    }

    @Test fun `should output OffsetTime using extension function`() = runBlocking {
        val capture = CoCapture()
        val offsetTime = OffsetTime.of(8, 27, 55, 544_233_100, ZoneOffset.ofHours(-5))
        capture.outputOffsetTime(offsetTime)
        capture.toString() shouldBe "08:27:55.544233100-05:00"
    }

    @Test fun `should output LocalDateTime using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        LocalDateTime.parse("2022-04-07T18:32:47.544").let { localDateTime ->
            coOutputLocalDateTime(localDateTime) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "2022-04-07T18:32:47.544"
        }
        i = 0
        LocalDateTime.parse("1999-04-01T08:45").let { localDateTime ->
            coOutputLocalDateTime(localDateTime) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "1999-04-01T08:45:00"
        }
    }

    @Test fun `should output LocalDateTime using extension function`() = runBlocking {
        val capture = CoCapture()
        LocalDateTime.parse("2022-04-07T18:32:47.544").let { localDateTime ->
            capture.outputLocalDateTime(localDateTime)
            capture.toString() shouldBe "2022-04-07T18:32:47.544"
        }
        capture.reset()
        LocalDateTime.parse("1999-04-01T08:45").let { localDateTime ->
            capture.outputLocalDateTime(localDateTime)
            capture.toString() shouldBe "1999-04-01T08:45:00"
        }
    }

    @Test fun `should output LocalDate using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        LocalDate.of(2022, 4, 7).let { localDate ->
            coOutputLocalDate(localDate) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "2022-04-07"
        }
        i = 0
        LocalDate.parse("1999-12-31").let { localDate ->
            coOutputLocalDate(localDate) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "1999-12-31"
            i = 0
            coOutputLocalDate(localDate.plusDays(1)) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "2000-01-01"
        }
    }

    @Test fun `should output LocalDate using extension function`() = runBlocking {
        val capture = CoCapture()
        LocalDate.of(2022, 4, 7).let { localDate ->
            capture.outputLocalDate(localDate)
            capture.toString() shouldBe "2022-04-07"
        }
        capture.reset()
        LocalDate.parse("1999-12-31").let { localDate ->
            capture.outputLocalDate(localDate)
            capture.toString() shouldBe "1999-12-31"
            capture.reset()
            capture.outputLocalDate(localDate.plusDays(1))
            capture.toString() shouldBe "2000-01-01"
        }
    }

    @Test fun `should output LocalTime using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        LocalTime.of(14, 3, 0).let { localTime ->
            coOutputLocalTime(localTime) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "14:03:00"
        }
        i = 0
        LocalTime.parse("09:31:27").let { localTime ->
            coOutputLocalTime(localTime) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "09:31:27"
            i = 0
            coOutputLocalTime(localTime.withNano(230000000)) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "09:31:27.230"
            i = 0
            coOutputLocalTime(localTime.withNano(234500000)) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "09:31:27.234500"
            i = 0
            coOutputLocalTime(localTime.withNano(234567890)) { charArray[i++] = it }
            String(charArray, 0, i) shouldBe "09:31:27.234567890"
        }
    }

    @Test fun `should output LocalTime using extension function`() = runBlocking {
        val capture = CoCapture()
        LocalTime.of(14, 3, 0).let { localTime ->
            capture.outputLocalTime(localTime)
            capture.toString() shouldBe "14:03:00"
        }
        capture.reset()
        LocalTime.parse("09:31:27").let { localTime ->
            capture.outputLocalTime(localTime)
            capture.toString() shouldBe "09:31:27"
            capture.reset()
            capture.outputLocalTime(localTime.withNano(230000000))
            capture.toString() shouldBe "09:31:27.230"
            capture.reset()
            capture.outputLocalTime(localTime.withNano(234500000))
            capture.toString() shouldBe "09:31:27.234500"
            capture.reset()
            capture.outputLocalTime(localTime.withNano(234567890))
            capture.toString() shouldBe "09:31:27.234567890"
        }
    }

    @Test fun `should output Year using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        coOutputYear(Year.of(2022)) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "2022"
    }

    @Test fun `should output Year using extension function`() = runBlocking {
        val capture = CoCapture()
        capture.outputYear(Year.of(2022))
        capture.toString() shouldBe "2022"
    }

    @Test fun `should output YearMonth using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        coOutputYearMonth(YearMonth.of(2022, 4)) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "2022-04"
    }

    @Test fun `should output YearMonth using extension function`() = runBlocking {
        val capture = CoCapture()
        capture.outputYearMonth(YearMonth.of(2022, 4))
        capture.toString() shouldBe "2022-04"
    }

    @Test fun `should output MonthDay using lambda`() = runBlocking {
        val charArray = CharArray(32)
        var i = 0
        coOutputMonthDay(MonthDay.of(4, 8)) { charArray[i++] = it }
        String(charArray, 0, i) shouldBe "--04-08"
    }

    @Test fun `should output MonthDay using extension function`() = runBlocking {
        val capture = CoCapture()
        capture.outputMonthDay(MonthDay.of(4, 8))
        capture.toString() shouldBe "--04-08"
    }

    class CoCapture(size: Int = 256) : CoOutput {

        private val array = CharArray(size)
        private var index = 0

        override suspend fun invoke(ch: Char) {
            array[index++] = ch
        }

        fun reset() {
            index = 0
        }

        override fun toString() = String(array, 0, index)

    }

}
