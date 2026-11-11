package ir.tadbir.app.core.util

import java.util.Calendar

data class JalaliDate(
    val year: Int,
    val month: Int,
    val day: Int
) {
    val key: String get() = String.format("%04d-%02d-%02d", year, month, day)

    val monthName: String get() = PERSIAN_MONTHS.getOrElse(month - 1) { "" }

    fun formatted(): String = "${toFa(day)} ${monthName} ${toFa(year)}"

    companion object {
        val PERSIAN_MONTHS = listOf(
            "فروردین", "اردیبهشت", "خرداد",
            "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر",
            "دی", "بهمن", "اسفند"
        )

        val WEEKDAY_NAMES = listOf(
            "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"
        )

        fun today(): JalaliDate {
            val cal = Calendar.getInstance()
            return gregorianToJalali(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        fun toFa(number: Number): String = toFa(number.toString())

        fun toFa(str: String): String {
            val sb = StringBuilder()
            for (ch in str) {
                if (ch in '0'..'9') {
                    sb.append((ch.code - '0'.code + 0x06F0).toChar())
                } else {
                    sb.append(ch)
                }
            }
            return sb.toString()
        }

        fun gregorianToJalali(gYear: Int, gMonth: Int, gDay: Int): JalaliDate {
            val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

            val gy = gYear - 1600
            val gm = gMonth - 1
            val gd = gDay - 1

            var gDayNo = 365 * gy + ((gy + 3) / 4) - ((gy + 99) / 100) + ((gy + 399) / 400)
            for (i in 0 until gm) {
                gDayNo += gDaysInMonth[i]
            }
            if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) {
                gDayNo++
            }
            gDayNo += gd

            var jDayNo = gDayNo - 79
            val jNp = jDayNo / 12053
            jDayNo %= 12053

            var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
            jDayNo %= 1461

            if (jDayNo >= 366) {
                jy += (jDayNo - 1) / 365
                jDayNo = (jDayNo - 1) % 365
            }

            var jm = 0
            for (i in 0..11) {
                if (jDayNo < jDaysInMonth[i]) {
                    jm = i + 1
                    break
                }
                jDayNo -= jDaysInMonth[i]
            }
            val jd = jDayNo + 1
            return JalaliDate(jy, jm, jd)
        }
    }
}
