package dev.sikri.shelter.sheets.models

import dev.sikri.shelter.i18n.Legalization
import dev.sikri.shelter.i18n.l10n.Localization
import io.github.sikrinick.geshikt.dsl.component.CellFormat
import io.github.sikrinick.geshikt.dsl.values.CellRangeReference
import dev.sikri.shelter.sheets.people.PeopleSheetBuilderModel
import dev.sikri.shelter.sheets.people.PeopleSheetModel
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

sealed class PeopleColumn(
    val header: String,
    val format: CellFormat,
    val builderProperty: KMutableProperty1<PeopleSheetBuilderModel, CellRangeReference?>,
    val modelProperty: KProperty1<PeopleSheetModel, CellRangeReference?>,
)

data class IdColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.id, CellFormat.Text,
    PeopleSheetBuilderModel::id, PeopleSheetModel::id
)

data class FamilyIdColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.familyId, CellFormat.Number,
    PeopleSheetBuilderModel::familyId, PeopleSheetModel::familyId
)

data class RoomColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.room, CellFormat.Text,
    PeopleSheetBuilderModel::room, PeopleSheetModel::room
)

data class FirstNameColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.firstName, CellFormat.Text,
    PeopleSheetBuilderModel::firstName, PeopleSheetModel::firstName
)

data class LastNameColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.lastName, CellFormat.Text,
    PeopleSheetBuilderModel::lastName, PeopleSheetModel::lastName
)

data class DocumentTypeColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.documentType, CellFormat.Text,
    PeopleSheetBuilderModel::documentType, PeopleSheetModel::documentType
)

data class DocumentIdColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.documentId, CellFormat.Text,
    PeopleSheetBuilderModel::documentId, PeopleSheetModel::documentId
)

data class AsylumDocumentIdColumn(
    val legalization: Legalization,
) : PeopleColumn(
    legalization.asylumDocumentIdName, CellFormat.Text,
    PeopleSheetBuilderModel::pesel, PeopleSheetModel::pesel
)

data class RegionColumn(
    val legalization: Legalization,
) : PeopleColumn(
    legalization.regionName, CellFormat.Text,
    PeopleSheetBuilderModel::region, PeopleSheetModel::region
)

data class CityColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.settlement, CellFormat.Text,
    PeopleSheetBuilderModel::city, PeopleSheetModel::city
)

data class BirthdateColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.birthDate, CellFormat.Date(),
    PeopleSheetBuilderModel::birthdate, PeopleSheetModel::birthdate
)

data class SexColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.sex, CellFormat.Text,
    PeopleSheetBuilderModel::sex, PeopleSheetModel::sex,
)

data class BorderCrossDateColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.borderCrossDate, CellFormat.Date(),
    PeopleSheetBuilderModel::borderCrossDate, PeopleSheetModel::borderCrossDate,
)

data class CheckInDateColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.checkInDate, CellFormat.Date(),
    PeopleSheetBuilderModel::checkInDate, PeopleSheetModel::checkInDate,
)

data class CheckOutDateColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.checkOutDate, CellFormat.Date(),
    PeopleSheetBuilderModel::checkOutDate, PeopleSheetModel::checkOutDate,
)

data class PhoneNumberColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.phoneNumber, CellFormat.Text,
    PeopleSheetBuilderModel::phoneNumber, PeopleSheetModel::phoneNumber
)

data class RemarksColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.additionalInfo, CellFormat.Text,
    PeopleSheetBuilderModel::remarks, PeopleSheetModel::remarks
)

data class AgeColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.ageAutomatic, CellFormat.Number,
    PeopleSheetBuilderModel::age, PeopleSheetModel::age
)

data class AgeCategoryColumn(
    val l10n: Localization,
) : PeopleColumn(
    l10n.columns.ageCategoryAutomatic, CellFormat.Text,
    PeopleSheetBuilderModel::ageCategories, PeopleSheetModel::ageCategories
)
