package dev.sikri.shelter

import dev.sikri.shelter.i18n.Legalization
import dev.sikri.shelter.i18n.l10n.Localization
import io.github.sikrinick.geshikt.DefaultContext
import io.github.sikrinick.geshikt.ProcessingContext
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.CellFormat
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.modifiers.border
import io.github.sikrinick.geshikt.dsl.component.style.HAlign
import io.github.sikrinick.geshikt.dsl.component.style.halign
import io.github.sikrinick.geshikt.dsl.component.style.text.bold
import io.github.sikrinick.geshikt.dsl.values.lazyColumn
import io.github.sikrinick.geshikt.runtime.process
import dev.sikri.shelter.sheets.calculation.calculationSheet
import dev.sikri.shelter.sheets.models.PeopleColumn
import dev.sikri.shelter.sheets.overview
import dev.sikri.shelter.sheets.people.allPeopleSheet
import dev.sikri.shelter.sheets.people.currentPeopleSheet
import dev.sikri.shelter.sheets.rooms.Room
import dev.sikri.shelter.sheets.rooms.rooms
import dev.sikri.shelter.sheets.technical
import dev.sikri.shelter.sheets.technicalSetup

@DslMarker
annotation class GShelterDsl

class GShelter(
    val context: ProcessingContext
)

@GShelterDsl
fun GShelter.create(
    id: Spreadsheet.Id,
    l10n: Localization,
    legalization: Legalization,
    columns: List<PeopleColumn>,
    rooms: List<Room>
) {
    process(context) {
        spreadsheet(id) {

            val columnNames = columns.map { it.header }

            val roomNames = lazyColumn(
                l10n.technical.roomNumbers.replace(" ", "")
            ) {
                rooms.forEach {
                    cell(
                        it.name,
                        cellFormat = CellFormat.Text,
                        modifier = Modifier.halign(HAlign.Type.Center).bold(true).border(right = true)
                    )
                }
            }

            val technicalSheet =
                technical(context, technicalSetup(l10n, legalization, roomNames.reference, columnNames))
            val allPeopleSheet = allPeopleSheet(context, l10n, columns, technicalSheet)
            val currentPeopleSheet = currentPeopleSheet(context, l10n, columns, allPeopleSheet)
            val roomSheet = rooms(l10n, rooms, roomNames, currentPeopleSheet, technicalSheet)
            calculationSheet(context, l10n, columns, allPeopleSheet, technicalSheet)

            overview(
                l10n = l10n,
                technical = technicalSheet,
                allPeopleSheetModel = allPeopleSheet,
                currentPeopleSheetModel = currentPeopleSheet,
                roomSheetModel = roomSheet
            )
        }
    }
}

@GShelterDsl
fun gShelter(
    context: ProcessingContext = DefaultContext,
    block: GShelter.() -> Unit
) {
    GShelter(context).block()
}