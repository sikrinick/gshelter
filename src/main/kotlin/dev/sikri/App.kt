package dev.sikri

import dev.sikri.shelter.create
import dev.sikri.shelter.gShelter
import dev.sikri.shelter.i18n.Legalization
import dev.sikri.shelter.i18n.UkrainianInPoland
import dev.sikri.shelter.i18n.UkrainianInUkraine
import dev.sikri.shelter.i18n.l10n.Polish
import dev.sikri.shelter.sheets.models.*
import dev.sikri.shelter.sheets.rooms.Room
import io.github.sikrinick.geshikt.DefaultContext
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.id

fun main() {
    createSpreadsheet(
        id = id("1euvDCejeiPUm26KV3UKSSf2rOi2-EprraeFtZVBQ6sQ"),
        legalization = UkrainianInPoland(
            localization = Polish
        )
    )
    createSpreadsheet(
        id = id("1u7_RB4aPq0VpiaSH-xWns9u5tdLc-1J3JeDzEwXVyeQ"),
        legalization = UkrainianInUkraine
    )
}

private fun createSpreadsheet(
    id: Spreadsheet.Id,
    legalization: Legalization,
) {
    val l10n = legalization.localization
    gShelter(
        context = DefaultContext.copy(
            maxRowCount = 2999
        )
    ) {
        create(
            id = id,
            l10n = l10n,
            legalization = legalization,
            columns = listOf(
                RoomColumn(l10n),
                FirstNameColumn(l10n),
                LastNameColumn(l10n),
                DocumentTypeColumn(l10n),
                DocumentIdColumn(l10n),
                AsylumDocumentIdColumn(legalization),
                RegionColumn(legalization),
                CityColumn(l10n),
                BirthdateColumn(l10n),
                SexColumn(l10n),
                BorderCrossDateColumn(l10n),
                CheckInDateColumn(l10n),
                PhoneNumberColumn(l10n),
                RemarksColumn(l10n),
                CheckOutDateColumn(l10n),
                AgeColumn(l10n),
                AgeCategoryColumn(l10n),
            ),
            rooms = listOf(
                Room("205", 4),
                Room("206", 2),
                Room("207", 4),
                Room("208", 7),
                Room("209", 7),
                Room("210", 4),
                Room("211", 4),
                Room("303", 8),
                Room("305", 3),
                Room("306", 3),
                Room("307", 5),
                Room("308", 4),
                Room("309", 3),
                Room("310", 3),
                Room("311", 5),
                Room("312", 2),
                Room("313", 5),
                Room("314", 3),
                Room("315", 3),
                Room("316", 3),
                Room("317", 3),
                Room("318", 3),
                Room("319", 3),
                Room("320", 6),
                Room("321", 7),
                Room("322", 4),
                Room("323", 3),
                Room("324", 2),
                Room("325", 4),
                Room("326", 7),
                Room("327", 4),
                Room("328", 8),
                Room("329", 7),
            )
        )
    }
}
