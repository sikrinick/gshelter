package dev.sikri.shelter.sheets

import dev.sikri.shelter.i18n.Legalization
import dev.sikri.shelter.i18n.l10n.Localization
import io.github.sikrinick.geshikt.ProcessingContext
import io.github.sikrinick.geshikt.dsl.Sheet
import io.github.sikrinick.geshikt.dsl.Spreadsheet
import io.github.sikrinick.geshikt.dsl.component.Line
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.style.text.bold
import io.github.sikrinick.geshikt.dsl.utils.asSheetRefName
import io.github.sikrinick.geshikt.dsl.utils.valueColumn
import io.github.sikrinick.geshikt.dsl.values.*

data class Technical(
    val ageCategories: AgeCategories,
    val sex: Sex,
    val documentTypes: CellRangeReference,
    val regions: CellRangeReference,
    val rooms: CellRangeReference,
    val sorting: CellRangeReference,

    val allKeyword: CellReference,
    val ageCategoriesForFiltering: CellRangeReference,
    val sexesForFiltering: CellRangeReference,
    val documentTypesForFiltering: CellRangeReference,
    val regionsForFiltering: CellRangeReference,
    val roomsForFiltering: CellRangeReference,

    val ordering: Ordering
) {
    data class AgeCategories(
        val all: CellRangeReference,
        val adult: CellReference,
        val teen: CellReference,
        val child: CellReference
    )

    data class Sex(
        val all: CellRangeReference,
        val male: CellReference,
        val female: CellReference
    )

    data class Ordering(
        val all: CellRangeReference,
        val ascending: CellReference,
        val descending: CellReference
    )
}

fun Spreadsheet.technical(
    context: ProcessingContext,
    technicalSheetSetup: TechnicalSheetSetup
): Technical {
    var technical: Technical? = null
    with(technicalSheetSetup) {
        sheet(name, modifier = Sheet.Modifier.hidden(hidden = true)) {
            row {

                // Age
                val dCell = with(ageCategories) {
                    lazyCell(value = d.value, named = d.value.asSheetRefName(parent = header))
                }
                val xCell = with(ageCategories) {
                    lazyCell(value = x.value, named = x.value.asSheetRefName(parent = header))
                }
                val zCell = with(ageCategories) {
                    lazyCell(value = z.value, named = z.value.asSheetRefName(parent = header))
                }
                val ageCatRefs = lazyColumn(ageCategories.header.asSheetRefName()) {
                    dCell()
                    xCell()
                    zCell()
                }
                column {
                    cell(ageCategories.header, Modifier.bold())
                    ageCatRefs()
                }
                column {
                    with(ageCategories) {
                        cell(descriptionHeader, Modifier.bold())
                        cell(d.description)
                        cell(x.description)
                        cell(z.description)
                    }
                }

                // Sex
                val mCell = with(sexes) {
                    lazyCell(value = male, named = male.asSheetRefName(parent = header))
                }
                val fCell = with(sexes) {
                    lazyCell(value = female, named = female.asSheetRefName(parent = header))
                }
                val sexesRefs = valueColumn(sexes.header, named = sexes.header.asSheetRefName(), Modifier.bold()) {
                    mCell()
                    fCell()
                }
                val documentTypeRefs = addCategory(context, documentTypes)
                val regionRefs = addCategory(context, regions)

                // Redirect to rooms
                val roomValues = rooms.refToRooms
                column {
                    cell(rooms.header, Modifier.bold())
                    cell { arrayformula(rooms.refToRooms) }
                    space(context.maxRowCount - height)
                }

                val sorting = addCategory(context, sorting)

                val allKeyword = lazyCell(
                    value = technicalSheetSetup.allKeyword,
                    modifier = Modifier.bold(),
                    named = technicalSheetSetup.allKeyword
                )
                column {
                    allKeyword()
                }

                val ageCategoriesForFiltering =
                    addFiltering(context, ageCategories.header, ageCatRefs.reference, this@with)
                val sexesForFiltering = addFiltering(context, sexes.header, sexesRefs, this@with)
                val documentTypesForFiltering = addFiltering(context, documentTypes.header, documentTypeRefs, this@with)
                val regionsForFiltering = addFiltering(context, regions.header, regionRefs, this@with)
                val roomsForFiltering = addFiltering(context, rooms.header, roomValues, this@with)

                val ascendingRef = with(ordering) {
                    lazyCell(value = ascending, named = ascending.asSheetRefName(parent = header))
                }
                val descendingRef = with(ordering) {
                    lazyCell(value = descending, named = descending.asSheetRefName(parent = header))
                }
                val orderingRefs = valueColumn(ordering.header, ordering.header.asSheetRefName(), Modifier.bold()) {
                    ascendingRef()
                    descendingRef()
                }

                technical = Technical(
                    ageCategories = Technical.AgeCategories(
                        all = ageCatRefs.reference,
                        adult = dCell.reference,
                        teen = xCell.reference,
                        child = zCell.reference
                    ),
                    sex = Technical.Sex(
                        all = sexesRefs,
                        male = mCell.reference,
                        female = fCell.reference
                    ),
                    documentTypes = documentTypeRefs,
                    regions = regionRefs,
                    rooms = roomValues,
                    sorting = sorting,

                    allKeyword = allKeyword.reference,
                    ageCategoriesForFiltering = ageCategoriesForFiltering,
                    sexesForFiltering = sexesForFiltering,
                    documentTypesForFiltering = documentTypesForFiltering,
                    regionsForFiltering = regionsForFiltering,
                    roomsForFiltering = roomsForFiltering,

                    ordering = Technical.Ordering(
                        all = orderingRefs,
                        ascending = ascendingRef.reference,
                        descending = descendingRef.reference
                    )
                )
            }
        }
    }
    return technical!!
}

private fun Line.addCategory(
    context: ProcessingContext,
    category: Category
): CellRangeReference {
    val categoryValues = lazyColumn(category.header.asSheetRefName()) {
        category.values.forEach {
            cell(it)
        }
        if (category.sizeLimited.not()) {
            val spaceSize = context.maxRowCount - height - 1
            if (spaceSize > 0) {
                space(size = spaceSize)
            }
        }
    }
    column {
        cell(category.header, Modifier.bold())
        categoryValues()
    }
    return categoryValues.reference
}

private fun Line.addFiltering(
    context: ProcessingContext,
    header: String,
    cellRange: CellRangeReference,
    technicalSheetSetup: TechnicalSheetSetup
): CellRangeReference {
    val filteringHeader = "$header ${technicalSheetSetup.filteringSuffix}"
    return valueColumn(
        header = filteringHeader,
        named = filteringHeader.asSheetRefName(),
        Modifier.bold(),
    ) {
        cell(technicalSheetSetup.allKeyword)
        cell { arrayformula(cellRange) }
        space(context.maxRowCount - height - 1)
    }
}

data class TechnicalSheetSetup(
    val name: String,
    val ageCategories: AgeCategories,
    val sexes: Sexes,
    val documentTypes: Category,
    val regions: Category,

    val rooms: Rooms,
    val sorting: Category,
    val allKeyword: String,
    val filteringSuffix: String,
    val ordering: Ordering,
) {
    data class AgeCategories(
        val header: String,
        val descriptionHeader: String,
        val d: Category,
        val x: Category,
        val z: Category
    ) {
        data class Category(val value: String, val description: String)
    }

    data class Sexes(
        val header: String,
        val male: String,
        val female: String
    )

    data class Rooms(
        val header: String,
        val refToRooms: CellRangeReference
    )
}

data class Category(val header: String, val sizeLimited: Boolean, val values: List<String>)
data class Ordering(val header: String, val ascending: String, val descending: String)

fun technicalSetup(
    l10n: Localization,
    legalization: Legalization,
    rooms: CellRangeReference, peopleColumns: List<String>
) = with(l10n.technical) {
    TechnicalSheetSetup(
        name = sheetName,
        ageCategories = TechnicalSheetSetup.AgeCategories(
            header = ageCategories,
            descriptionHeader = ageCategoriesDescr,
            d = TechnicalSheetSetup.AgeCategories.Category(
                ageCategoryAdult, ageCategoryAdultDescr
            ),
            x = TechnicalSheetSetup.AgeCategories.Category(
                ageCategoryTeen, ageCategoryTeenDescr,
            ),
            z = TechnicalSheetSetup.AgeCategories.Category(
                ageCategoryChild, ageCategoryChildDescr,
            ),
        ),
        sexes = TechnicalSheetSetup.Sexes(
            sex,
            male = sexMale,
            female = sexFemale
        ),
        documentTypes = Category(
            documentTypes,
            false,
            legalization.documentTypes,
        ),
        regions = Category(
            legalization.regionName,
            true,
            legalization.regions,
        ),

        rooms = TechnicalSheetSetup.Rooms(
            roomNumbers,
            rooms
        ),

        sorting = Category(
            sortBy,
            true,
            peopleColumns
        ),

        allKeyword = allKeyword,
        filteringSuffix = filteringSuffix,
        ordering = Ordering(
            orderBy,
            ascending,
            descending,
        )
    )
}
