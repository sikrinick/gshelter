package dev.sikri.shelter.sheets.calculation

import dev.sikri.shelter.i18n.l10n.Localization
import dev.sikri.shelter.sheets.models.*
import io.github.sikrinick.geshikt.ProcessingContext
import io.github.sikrinick.geshikt.dsl.Sheet
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.modifiers.allBorders
import io.github.sikrinick.geshikt.dsl.component.modifiers.named
import io.github.sikrinick.geshikt.dsl.component.modifiers.validateOneOf
import io.github.sikrinick.geshikt.dsl.component.style.colored
import io.github.sikrinick.geshikt.dsl.component.style.text.bold
import io.github.sikrinick.geshikt.dsl.component.style.text.fontSize
import io.github.sikrinick.geshikt.dsl.utils.asSheetRefName
import io.github.sikrinick.geshikt.dsl.values.CellRangeReference
import io.github.sikrinick.geshikt.dsl.values.CellReference
import io.github.sikrinick.geshikt.dsl.values.invoke
import io.github.sikrinick.geshikt.dsl.values.lazyColumn
import dev.sikri.shelter.sheets.Technical
import dev.sikri.shelter.sheets.people.AllPeopleSheetModel
import dev.sikri.shelter.sheets.style.Colors

fun Spreadsheet.calculationSheet(
    context: ProcessingContext,
    l10n: Localization,
    peopleColumns: List<PeopleColumn>,
    allPeopleSheetModel: AllPeopleSheetModel,
    technical: Technical
) = with(l10n.calculation) {
    val sheetName = sheetName
    val step = 5
    val filterRows = peopleColumns.windowed(size = step, step = step, partialWindows = true)
    val rowsToHide = List(filterRows.size) { idx -> idx * 4 + 2 }
        .let { rowsToHide ->
            val lastRow = rowsToHide.last()
            val sortingLogic = lastRow + 4
            val sqlQueryHeader = sortingLogic + 3
            val sqlQueryValue = sortingLogic + 4
            rowsToHide + listOf(sortingLogic, sqlQueryHeader, sqlQueryValue)
        }

    sheet(sheetName, Sheet.Modifier.hideRows(rowsToHide)) {
        column {
            val queries = mutableListOf<CellReference>()

            filterRows.forEachIndexed { windowIdx, peopleColumns ->
                row {
                    column(Modifier.allBorders().bold(true)) {
                        cell("$filters ${windowIdx + 1}")
                        cell(values)
                        cell("SQL Condition")
                    }

                    peopleColumns
                        .map { it.calculationColumn(technical, l10n) }
                        .flatMapIndexed { idx, col ->
                            col.buildForCalculation(sheetName, colIdx = (step * windowIdx) + idx, technical).invoke(this)
                        }
                        .let(queries::addAll)
                }
                space(1)
            }
            var sortingRef: CellReference? = null
            var orderingRef: CellReference? = null
            row {
                column {
                    cell(sortBy, Modifier.allBorders().bold(true))
                    val sortingValue = cell(Modifier.validateOneOf(technical.sorting).allBorders().bold(true))
                    sortingRef = cell(Modifier.allBorders().bold(true)) {
                        iferror(
                            join(
                                " ",
                                "ORDER BY".type(),
                                switch(
                                    sortingValue,
                                    *peopleColumns
                                        .mapIndexed { idx, it ->
                                            it.header.type() to "Col${idx + 1}".type()
                                        }
                                        .toTypedArray()
                                )
                            ),
                            "ORDER BY Col1"
                        )
                    }.reference
                }
                column {
                    cell(orderBy, Modifier.allBorders().bold(true))
                    val orderingValue = cell(Modifier.validateOneOf(technical.ordering.all).allBorders().bold(true))
                    orderingRef = cell(Modifier.allBorders().bold(true)) {
                        iferror(
                            switch(
                                orderingValue,
                                technical.ordering.ascending.type() to "ASC".type(),
                                technical.ordering.descending.type() to "DESC".type(),
                            ),
                            "ASC"
                        )
                    }.reference
                }
            }

            space(2)

            cell("SQL Query", Modifier.allBorders().bold(true))

            val firstNameIdx = peopleColumns.indexOfFirst { it is FirstNameColumn }
            val sqlQuery = cell(Modifier.allBorders().bold(true).named("CalculationSqlQuery")) {
                join(
                    " ",
                    join(
                        " AND ",
                        List(peopleColumns.size, init = { it + 1 })
                            .joinToString(
                                prefix = "SELECT ",
                                transform = { "Col${it}" },
                                separator = ", ",
                                postfix = " WHERE Col${firstNameIdx + 1} IS NOT NULL"
                            )
                            .type(),
                        *queries.map { it.type() }.toTypedArray()
                    ),
                    sortingRef!!.type(),
                    orderingRef!!.type()
                )
            }

            space(1)

            val firstNameResults = lazyColumn(named = "CalculationResultFirstName".asSheetRefName(sheetName)) {
                space(context.maxRowCount)
            }
            row {
                cell(quantity, Modifier.bold(true).fontSize(18))
                cell(Modifier.bold(true).fontSize(18)) {
                    counta(firstNameResults.reference)
                }
            }
            row {
                peopleColumns.forEach {
                    cell(it.header, Modifier.allBorders().bold(true).colored(Colors.lightestGreen))
                }
            }
            row {
                cell {
                    //iferror(
                        query(
                            arrayliteral {
                                row {
                                    columns(
                                        *peopleColumns
                                            .mapNotNull { it.modelProperty.get(allPeopleSheetModel) }
                                            .map { it.type() }
                                            .toTypedArray()
                                    )
                                }
                            },
                            sqlQuery.reference
                        )
                    //    ""
                    //)
                }
                val skip = firstNameIdx - 1
                if (skip > 0) space(skip)
                firstNameResults()
            }
        }
    }
}

private fun PeopleColumn.calculationColumn(
    technical: Technical,
    l10n: Localization,
) = when(this) {
    is AgeCategoryColumn -> equatableCalculationColumn(technical.ageCategoriesForFiltering)
    is DocumentTypeColumn -> equatableCalculationColumn(technical.documentTypesForFiltering)
    is RegionColumn -> equatableCalculationColumn(technical.regionsForFiltering)
    is RoomColumn -> equatableCalculationColumn(technical.roomsForFiltering)
    is SexColumn -> equatableCalculationColumn(technical.sexesForFiltering)

    is AgeColumn -> comparableCalculationColumn(l10n)
    is BirthdateColumn -> comparableCalculationColumn(l10n)
    is BorderCrossDateColumn -> comparableCalculationColumn(l10n)
    is CheckInDateColumn -> comparableCalculationColumn(l10n)
    is CheckOutDateColumn -> comparableCalculationColumnWithEmptyByDefault()

    else -> equatableCalculationColumn()
}

private fun PeopleColumn.equatableCalculationColumn(
    filteringValues: CellRangeReference? = null
) = DefaultCalculationColumn(
    header, format,
    CalculationColumn.Operator.EQ,
    filteringValues
)

private fun PeopleColumn.comparableCalculationColumn(
    l10n: Localization,
    filteringValues: CellRangeReference? = null
) = MultipleCalculationColumns(
    DefaultCalculationColumn(
        "${header}, ${l10n.calculation.from}", format,
        CalculationColumn.Operator.GE,
        filteringValues
    ),
    DefaultCalculationColumn(
        "${header}, ${l10n.calculation.to}", format,
        CalculationColumn.Operator.LE,
        filteringValues
    )
)

private fun PeopleColumn.comparableCalculationColumnWithEmptyByDefault(
    filteringValues: CellRangeReference? = null
) = ComparableCalculationColumnWithEmptyByDefault(header, format, filteringValues)
