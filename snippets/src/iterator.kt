import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// iterate LocalDateTime by minute
// closed-ended range (..)
operator fun ClosedRange<LocalDateTime>.iterator(): Iterator<LocalDateTime> =
    object : Iterator<LocalDateTime> {
        private var current = start

        override fun hasNext(): Boolean = current <= endInclusive

        override fun next(): LocalDateTime {
            val nextValue = current
            current = current.plusMinutes(1)
            return nextValue
        }
    }

// iterate LocalDateTime by minute
// open-ended range (..<)
operator fun OpenEndRange<LocalDateTime>.iterator(): Iterator<LocalDateTime> =
    object : Iterator<LocalDateTime> {
        private var current = start

        override fun hasNext(): Boolean = current < endExclusive

        override fun next(): LocalDateTime {
            val nextValue = current
            current = current.plusMinutes(1)
            return nextValue
        }
    }

// convenience function to create LocalDateTime from string of YYYY-MM-DD HH24:MI
fun localDateTimeOf(string: String): LocalDateTime {
    val (date, time) = string.split(" ")
    return LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time))
}

fun main() {
    run {
        println("#1 ---")
        val start = LocalDateTime.of(LocalDate.of(2025, 6, 30),LocalTime.of(23, 54))
        val end = LocalDateTime.of(LocalDate.of(2025, 7, 1), LocalTime.of(0, 7))
        val range = start .. end
        for (time in range) {
            println(time)
        }
    }

    run {
        println("#2 ---")
        val range =
            LocalDateTime.of(LocalDate.parse("2025-06-30"), LocalTime.parse("23:54")) ..
            LocalDateTime.of(LocalDate.parse("2025-07-01"), LocalTime.parse("00:07"))
        for (time in range) {
            println(time)
        }
    }

    run {
        println("#3 ---")
        val range = localDateTimeOf("2025-06-30 23:54") ..< localDateTimeOf("2025-07-01 00:07")
        for (time in range) {
            println(time)
        }
    }
}