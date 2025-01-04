package dev.sikri.shelter.sheets.people

import dev.sikri.shelter.i18n.l10n.Localization
import dev.sikri.shelter.sheets.models.*
import io.github.sikrinick.geshikt.ProcessingContext
import io.github.sikrinick.geshikt.dsl.Sheet
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.modifiers.basicFilter
import io.github.sikrinick.geshikt.dsl.component.modifiers.validateOneOf
import io.github.sikrinick.geshikt.dsl.types.DateUnit
import io.github.sikrinick.geshikt.dsl.utils.asSheetRefName
import io.github.sikrinick.geshikt.dsl.utils.valueColumn
import io.github.sikrinick.geshikt.dsl.values.PositionedCellRangeReference
import io.github.sikrinick.geshikt.dsl.values.first
import dev.sikri.shelter.sheets.Technical

typealias AllPeopleSheetModel = PeopleSheetModel

fun Spreadsheet.allPeopleSheet(
    context: ProcessingContext,
    l10n: Localization,
    oldPeopleColumns: List<PeopleColumn>,
    technical: Technical
): AllPeopleSheetModel {
    var allPeopleSheetBuilder: PeopleSheetBuilderModel? = null
    val sheetName = l10n.people.sheetName
    val freezeColumns = oldPeopleColumns
        .indexOfLast {
            it is IdColumn || it is FamilyIdColumn || it is RoomColumn
        } + 1
    sheet(
        sheetName,
        Sheet.Modifier.freezeRows(1).freezeColumns(freezeColumns).hideEmptyColumns(),
        Modifier.basicFilter()
    ) {
        row {
            var birthdates: PositionedCellRangeReference? = null
            var ages: PositionedCellRangeReference? = null

            allPeopleSheetBuilder = oldPeopleColumns.fold(PeopleSheetBuilderModel()) { builder, column ->
                val values = valueColumn(column.header, named = column.header.asSheetRefName(sheetName)) {
                    val times = context.maxRowCount - height
                    val modifier = column.validationModifier(technical)
                    val cellFormat = column.format

                    when (column) {
                        is AgeColumn -> {
                            ages = repeatCell(times, modifier, cellFormat = cellFormat) {
                                val age = datedif(birthdates!!.first(), today(), DateUnit.Years)
                                `if`(
                                    condition = age isLessThan 120,
                                    ifTrue = age,
                                    ifFalse = ""
                                )
                            } as PositionedCellRangeReference
                        }

                        is AgeCategoryColumn -> {
                            val age = ages!!.first()
                            repeatCell(times, modifier, cellFormat = cellFormat) {
                                `if`(
                                    regexMatch(text(age, "#,###"), "\\w+"),
                                    `if`(
                                        age isGreaterThanOrEqualsTo 18,
                                        technical.ageCategories.adult.type(),
                                        `if`(
                                            age isGreaterThanOrEqualsTo 11,
                                            technical.ageCategories.teen,
                                            technical.ageCategories.child
                                        )
                                    ),
                                    `if`(
                                        age isEqualTo 0,
                                        technical.ageCategories.child,
                                        ""
                                    )
                                )
                            }
                        }

                        else -> {
                            repeatCell(times, modifier, cellFormat = cellFormat).also {
                                if (column is BirthdateColumn) {
                                    birthdates = it as PositionedCellRangeReference
                                }
                            }
                        }
                    }


                }
                column.builderProperty.set(receiver = builder, value = values)
                builder
            }
        }
    }
    return allPeopleSheetBuilder!!.build()
}

private fun PeopleColumn.validationModifier(technical: Technical) = when (this) {
    is RoomColumn -> Modifier.validateOneOf(technical.rooms)
    is DocumentTypeColumn -> Modifier.validateOneOf(technical.documentTypes)
    is SexColumn -> Modifier.validateOneOf(technical.sex.all)
    is RegionColumn -> Modifier.validateOneOf(technical.regions)
    is AgeCategoryColumn -> Modifier.validateOneOf(technical.ageCategories.all)
    else -> Modifier.None
}
