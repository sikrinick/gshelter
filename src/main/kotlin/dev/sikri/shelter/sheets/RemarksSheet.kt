package dev.sikri.shelter.sheets

import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.style.StandardColors
import io.github.sikrinick.geshikt.dsl.component.style.Wrap
import io.github.sikrinick.geshikt.dsl.component.style.text.fontSize
import io.github.sikrinick.geshikt.dsl.component.style.text.textColor
import io.github.sikrinick.geshikt.dsl.component.style.wrap

fun Spreadsheet.remarks() {
    sheet("УВАГА!") {
        val modifier = Modifier
            .fontSize(28)
            .textColor(StandardColors.red)
            .wrap(Wrap.Type.Overflow)
        column {
            listOf(
                "Натискати кнопку Фільтра в таблиці Ludzie НЕ МОЖНА!",
                "Усувати строки в таблиці Ludzie НЕ МОЖНА",
                "Змінювати поля з формулами НЕ МОЖНА",
                "Змінювати щось, окрім Ilość Miejsc (кількість місць) в таблиці Pokoje НЕ МОЖНА"
            ).forEach {
                cell(it, modifier)
                space(1)
            }
        }
    }
}
