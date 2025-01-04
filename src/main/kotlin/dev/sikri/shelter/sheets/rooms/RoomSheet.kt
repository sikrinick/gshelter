package dev.sikri.shelter.sheets.rooms

import dev.sikri.shelter.i18n.l10n.Localization
import io.github.sikrinick.geshikt.dsl.Sheet
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.CellFormat
import io.github.sikrinick.geshikt.dsl.component.modifiers.ConditionalFormat
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.modifiers.basicFilter
import io.github.sikrinick.geshikt.dsl.component.modifiers.border
import io.github.sikrinick.geshikt.dsl.component.modifiers.formatBy
import io.github.sikrinick.geshikt.dsl.component.style.HAlign
import io.github.sikrinick.geshikt.dsl.component.style.StandardColors
import io.github.sikrinick.geshikt.dsl.component.style.colored
import io.github.sikrinick.geshikt.dsl.component.style.halign
import io.github.sikrinick.geshikt.dsl.component.style.text.bold
import io.github.sikrinick.geshikt.dsl.utils.columnWithHeader
import io.github.sikrinick.geshikt.dsl.utils.valueColumn
import io.github.sikrinick.geshikt.dsl.values.CellRangeReference
import io.github.sikrinick.geshikt.dsl.values.LazyColumn
import io.github.sikrinick.geshikt.dsl.values.PositionedCellRangeReference
import io.github.sikrinick.geshikt.dsl.values.invoke
import io.github.sikrinick.geshikt.dsl.values.toPositionedCellReferenceList
import dev.sikri.shelter.sheets.Technical
import dev.sikri.shelter.sheets.people.CurrentPeopleSheetModel
import dev.sikri.shelter.sheets.style.Colors

data class RoomSheetModel(
    val roomNames: CellRangeReference,
    val capacities: CellRangeReference,
    val occupied: CellRangeReference,
    val adultCount: CellRangeReference,
    val teenCount: CellRangeReference,
    val childrenCount: CellRangeReference,
    val vacant: CellRangeReference
)

fun Spreadsheet.rooms(
    l10n: Localization,
    rooms: List<Room>,
    roomNames: LazyColumn,
    currentPeopleSheet: CurrentPeopleSheetModel,
    technical: Technical
): RoomSheetModel = with(l10n.rooms) {

    lateinit var capacities: PositionedCellRangeReference
    lateinit var occupied: PositionedCellRangeReference
    lateinit var adultCount: PositionedCellRangeReference
    lateinit var teenCount: PositionedCellRangeReference
    lateinit var childrenCount: PositionedCellRangeReference
    lateinit var vacant: PositionedCellRangeReference

    sheet(
        sheetName,
        Sheet.Modifier.freezeRows(1).hideEmptyColumns().hideEmptyRows(),
        Modifier.basicFilter()
    ) {
        row {
            val roomNames = columnWithHeader(roomNumbers, Modifier.bold().border(right = true)) {
                roomNames()
            }
            capacities = valueColumn(
                placesQuantity,
                named = placesQuantity.removeSpaces(),
                Modifier.bold()
            ) {
                rooms.forEach {
                    cell(it.capacity, Modifier.halign(HAlign.Type.Center).bold())
                }
            }

            occupied = valueColumn(
                placesFilled,
                named = placesFilled.removeSpaces(),
                Modifier.bold()
            ) {
                column(occupiedModifier) {
                    roomNames.toPositionedCellReferenceList().forEach { roomName ->
                        cell(Modifier.halign(HAlign.Type.Right)) {
                            countIf(currentPeopleSheet.room) { it isEqualTo roomName }
                        }
                    }
                }
            }

            adultCount = valueColumn(
                adults, named = adults.removeSpaces(),
                Modifier.bold()
            ) {
                column(secondaryModifier) {
                    roomNames.toPositionedCellReferenceList().forEach { roomName ->
                        cell(Modifier.halign(HAlign.Type.Right)) {
                            countIf(
                                filter(
                                    currentPeopleSheet.room,
                                    currentPeopleSheet.room isEqualTo roomName,
                                    currentPeopleSheet.ageCategories isEqualTo technical.ageCategories.adult
                                )
                            ) { it isEqualTo roomName }
                        }
                    }
                }
            }

            teenCount = valueColumn(
                teens,
                named = teens.removeSpaces(),
                Modifier.bold()
            ) {
                column(secondaryModifier) {
                    roomNames.toPositionedCellReferenceList().forEach { roomName ->
                        cell(Modifier.halign(HAlign.Type.Right)) {
                            countIf(
                                filter(
                                    currentPeopleSheet.room,
                                    currentPeopleSheet.room isEqualTo roomName,
                                    currentPeopleSheet.ageCategories isEqualTo technical.ageCategories.teen
                                )
                            ) { it isEqualTo roomName }
                        }
                    }
                }
            }

            childrenCount = valueColumn(
                l10n.rooms.children,
                named = l10n.rooms.children.removeSpaces(),
                Modifier.bold()
            ) {
                column(secondaryModifier) {
                    roomNames.toPositionedCellReferenceList().forEach { roomName ->
                        cell(Modifier.halign(HAlign.Type.Right)) {
                            countIf(
                                filter(
                                    currentPeopleSheet.room,
                                    currentPeopleSheet.room isEqualTo roomName,
                                    currentPeopleSheet.ageCategories isEqualTo technical.ageCategories.child
                                )
                            ) { it isEqualTo roomName }
                        }
                    }
                }
            }

            vacant = valueColumn(
                placesEmpty,
                named = placesEmpty.removeSpaces(),
                Modifier.bold()
            ) {
                column(vacantModifier) {
                    capacities.toPositionedCellReferenceList()
                        .zip(occupied.toPositionedCellReferenceList()).forEach { (capacity, occupied) ->
                            cell(Modifier.halign(HAlign.Type.Right)) {
                                capacity - occupied
                            }
                        }
                }
            }

            columnWithHeader(
                comments,
                Modifier.bold()
            ) {
                repeatCell(times = rooms.size, Modifier.halign(HAlign.Type.Right), cellFormat = CellFormat.Text)
            }
            columnWithHeader(
                residentsInfoDoNotEdit,
                Modifier.bold()
            ) {
                roomNames.toPositionedCellReferenceList().forEach { roomName ->
                    cell(cellFormat = CellFormat.Text) {
                        iferror(
                            textjoin(
                                ", ",
                                true,
                                filter(
                                    currentPeopleSheet.remarks,
                                    currentPeopleSheet.room isEqualTo roomName
                                )
                            ),
                            ""
                        )
                    }
                }
            }
        }
    }
    return RoomSheetModel(
        roomNames = roomNames.reference,
        capacities = capacities,
        occupied = occupied,
        adultCount = adultCount,
        teenCount = teenCount,
        childrenCount = childrenCount,
        vacant = vacant
    )
}

private val occupiedModifier = Modifier.formatBy(
    ConditionalFormat(
        criterion = { it isEqualTo 0 },
        format = ConditionalFormat.ResultingFormat(bold = true, backgroundColor = Colors.lightGreen)
    ),
    ConditionalFormat(
        criterion = { it isGreaterThan 0 },
        format = ConditionalFormat.ResultingFormat(bold = true, backgroundColor = Colors.lightestGreen)
    ),
    ConditionalFormat(
        criterion = { it isLessThan 0 },
        format = ConditionalFormat.ResultingFormat(bold = true, textColor = StandardColors.Max.red)
    )
)
private val vacantModifier = Modifier.formatBy(
    ConditionalFormat(
        criterion = { it isEqualTo 0 },
        format = ConditionalFormat.ResultingFormat(bold = true, backgroundColor = Colors.softRed)
    ),
    ConditionalFormat(
        criterion = { it isGreaterThan 0 },
        format = ConditionalFormat.ResultingFormat(bold = true, backgroundColor = Colors.lightGreen)
    ),
    ConditionalFormat(
        criterion = { it isLessThan 0 },
        format = ConditionalFormat.ResultingFormat(bold = true, textColor = StandardColors.Max.red)
    )
)
private val secondaryModifier = Modifier.colored(Colors.lightestGreen)

private fun String.removeSpaces() = replace(" ", "")
