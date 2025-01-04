package dev.sikri.shelter.sheets.people

import dev.sikri.shelter.i18n.l10n.Localization
import io.github.sikrinick.geshikt.ProcessingContext
import io.github.sikrinick.geshikt.dsl.Sheet
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.utils.asSheetRefName
import io.github.sikrinick.geshikt.dsl.utils.valueColumn
import dev.sikri.shelter.sheets.models.PeopleColumn

typealias CurrentPeopleSheetModel = PeopleSheetModel

fun Spreadsheet.currentPeopleSheet(
    context: ProcessingContext,
    l10n: Localization,
    oldPeopleColumns: List<PeopleColumn>,
    allPeopleSheetModel: PeopleSheetModel
): CurrentPeopleSheetModel {
    var currentPeopleSheetBuilder: PeopleSheetBuilderModel? = null
    val sheetName = l10n.people.peopleCurrentSheetName
    sheet(sheetName, modifier = Sheet.Modifier.hidden(hidden = true)) {
        row {
            currentPeopleSheetBuilder = oldPeopleColumns.fold(PeopleSheetBuilderModel()) { builder, column ->
                with(column) {
                    val values = valueColumn(column.header, named = column.header.asSheetRefName(parent = sheetName)) {
                        cell {
                            arrayformula {
                                `if`(
                                    regexMatch(text(allPeopleSheetModel.checkOutDate, "#,###"), "\\w+"),
                                    "",
                                    column.modelProperty.get(allPeopleSheetModel)!!
                                )
                            }
                        }
                        space(context.maxRowCount - 1 - 1)
                    }
                    builderProperty.set(receiver = builder, values)
                    builder
                }
            }
        }
    }
    return currentPeopleSheetBuilder!!.build()
}
