package dev.sikri.shelter.sheets

import dev.sikri.shelter.i18n.l10n.Localization
import io.github.sikrinick.geshikt.dsl.Sheet
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.Column
import io.github.sikrinick.geshikt.dsl.component.layout.Size
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.modifiers.allBorders
import io.github.sikrinick.geshikt.dsl.component.style.*
import io.github.sikrinick.geshikt.dsl.component.style.text.bold
import io.github.sikrinick.geshikt.dsl.component.style.text.fontSize
import io.github.sikrinick.geshikt.dsl.component.style.text.textColor
import io.github.sikrinick.geshikt.dsl.types.Criterion
import io.github.sikrinick.geshikt.dsl.values.CellRangeReference
import dev.sikri.shelter.sheets.people.AllPeopleSheetModel
import dev.sikri.shelter.sheets.people.CurrentPeopleSheetModel
import dev.sikri.shelter.sheets.people.PeopleSheetModel
import dev.sikri.shelter.sheets.rooms.RoomSheetModel
import dev.sikri.shelter.sheets.style.Colors

fun Spreadsheet.overview(
    l10n: Localization,
    technical: Technical,
    allPeopleSheetModel: AllPeopleSheetModel,
    currentPeopleSheetModel: CurrentPeopleSheetModel,
    roomSheetModel: RoomSheetModel
) = with(l10n.overview) {
    sheet(sheetName, Sheet.Modifier.hideGrid(true).hideEmptyRows().hideEmptyColumns()) {
        row {
            column {
                sheetStatistics(l10n, roomSheetModel)

                space(4)

                peopleStatistics(l10n, peopleCurrent, technical, currentPeopleSheetModel)

                space(7)

                peopleStatistics(l10n, peopleTotal, technical, allPeopleSheetModel)

                space(1)
            }

            space(1)

            column {
                ageStatistics(l10n, byAge, technical, currentPeopleSheetModel)

                space(1)

                when (val allPeopleIds = allPeopleSheetModel.id) {
                    null -> space(2)
                    else -> nextId(nextAdultId, allPeopleIds)
                }

                space(1)

                when (val allFamilyIds = allPeopleSheetModel.familyId) {
                    null -> space(2)
                    else -> nextId(nextFamilyId, allFamilyIds)
                }

                space(3)

                ageStatistics(l10n, byAgeTotal, technical, allPeopleSheetModel)

                space(1)
            }

            space(1)
        }

    }
}

private fun Column.sheetStatistics(
    l10n: Localization,
    roomSheetModel: RoomSheetModel
) = with(l10n.overview) {
    column(Modifier.allBorders()) {
        cell(places, Modifier.bold(true), Size(height = 1, width = 3))
        row {
            cell(roomsQuantity, size = Size(height = 1, width = 2))
            cell(Modifier.bold(true)) {
                counta(roomSheetModel.roomNames)
            }
        }
        row {
            cell(roomPlacesTotal, size = Size(height = 1, width = 2))
            cell(Modifier.bold(true)) {
                sum(roomSheetModel.capacities)
            }
        }
        row {
            cell(roomPlacesFilled, size = Size(height = 1, width = 2))
            cell(Modifier.bold(true).textColor(Colors.darkRed)) {
                sum(roomSheetModel.occupied)
            }
        }
        row {
            cell(roomPlacesEmpty, size = Size(height = 1, width = 2))
            cell(Modifier.bold(true).textColor(Colors.darkGreen)) {
                sum(roomSheetModel.vacant)
            }
        }
    }
}

private fun Column.peopleStatistics(
    l10n: Localization,
    name: String,
    technical: Technical,
    anyPeopleSheetModel: PeopleSheetModel
) = with(l10n.overview) {
    with(anyPeopleSheetModel) {
        val adultCriterion: Criterion = { it isEqualTo technical.ageCategories.adult }
        val teenCriterion: Criterion = { it isEqualTo technical.ageCategories.teen }
        val childCriterion: Criterion = { it isEqualTo technical.ageCategories.child }

        val maleCriterion: Criterion = { it isEqualTo technical.sex.male }
        val femaleCriterion: Criterion = { it isEqualTo technical.sex.female }

        column(Modifier.allBorders()) {
            row {
                cell(name, Modifier.bold(true), Size(height = 1, width = 3))
                cell(sexWomen, Modifier.bold(true))
                cell(sexMen, Modifier.bold(true))
            }
            row {
                cell(total, size = Size(height = 1, width = 2))
                cell(Modifier.bold(true)) { sum(countIf(sex, maleCriterion), countIf(sex, femaleCriterion)) }
                cell { countIf(sex, femaleCriterion) }
                cell { countIf(sex, maleCriterion) }
            }
            row {
                cell(adults, size = Size(height = 1, width = 2))
                cell(Modifier.bold(true)) { countIf(ageCategories, adultCriterion) }
                cell { countIfs(sex to femaleCriterion, ageCategories to adultCriterion) }
                cell { countIfs(sex to maleCriterion, ageCategories to adultCriterion) }
            }
            row {
                column {
                    cell(
                        l10n.overview.children,
                        Modifier.halign(HAlign.Type.Left).valign(VAlign.Type.Top),
                        Size(height = 3, width = 1)
                    )
                }

                column {
                    row {
                        cell(total.replaceFirstChar { it.lowercase() })
                        cell(Modifier.bold(true)) {
                            countIf(ageCategories, teenCriterion) + countIf(
                                ageCategories,
                                childCriterion
                            )
                        }
                        cell {
                            countIfs(sex to femaleCriterion, ageCategories to teenCriterion) +
                                    countIfs(sex to femaleCriterion, ageCategories to childCriterion)
                        }
                        cell {
                            countIfs(sex to maleCriterion, ageCategories to teenCriterion) +
                                    countIfs(sex to maleCriterion, ageCategories to childCriterion)
                        }
                    }

                    listOf(
                        ">=11" to teenCriterion,
                        "<=10>" to childCriterion
                    ).forEach { (name, ageCriterion) ->
                        row {
                            cell(name)
                            cell(Modifier.bold(true)) { countIf(ageCategories, ageCriterion) }
                            cell { countIfs(sex to femaleCriterion, ageCategories to ageCriterion) }
                            cell { countIfs(sex to maleCriterion, ageCategories to ageCriterion) }
                        }
                    }
                }
            }
        }
    }
}

private fun Column.ageStatistics(
    l10n: Localization,
    name: String, technical: Technical, anyPeopleSheetModel: PeopleSheetModel
) = with(l10n.overview) {
    with(anyPeopleSheetModel) {
        column(Modifier.allBorders()) {
            cell(name, Modifier.bold(true), Size(width = 5, height = 1))
            row {
                cell(ageStatsFrom)
                cell(ageStatsTo)
                cell(total, Modifier.bold(true))
                cell(sexMen)
                cell(sexWomen)
            }
            listOf(
                0 to 1,
                2 to 5,
                6 to 10,
                11 to 14,
                15 to 17,
                18 to 65,
                66 to 1000
            ).forEach { (fromAge, toAge) ->
                row {
                    val from = cell(fromAge)
                    val to = cell(toAge)

                    val maleCriterion: Criterion = { it isEqualTo technical.sex.male }
                    val femaleCriterion: Criterion = { it isEqualTo technical.sex.female }
                    val fromCriterion: Criterion = { it isGreaterThanOrEqualTo from.reference }
                    val toCriterion: Criterion = { it isLessThanOrEqualTo to.reference }

                    cell(Modifier.bold(true)) { countIfs(age to fromCriterion, age to toCriterion) }
                    cell { countIfs(age to fromCriterion, age to toCriterion, sex to femaleCriterion) }
                    cell { countIfs(age to fromCriterion, age to toCriterion, sex to maleCriterion) }
                }
            }
        }
    }
}

private fun Column.nextId(name: String, ids: CellRangeReference) {
    cell(
        name, Modifier
            .allBorders()
            .bold(true)
            .valign(VAlign.Type.Middle)
            .halign(HAlign.Type.Center),
        Size(height = 1, width = 3)
    )
    cell(
        Modifier
            .allBorders()
            .bold(true)
            .valign(VAlign.Type.Middle)
            .halign(HAlign.Type.Center)
            .textColor(StandardColors.Max.red)
            .fontSize(36),
        Size(height = 3, width = 3)
    ) {
        max(ids) + 1
    }
}
