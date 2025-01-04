package dev.sikri.shelter.i18n.l10n

sealed interface Localization {
    val columns: ColumnsLocalization

    val overview: OverviewLocalization

    val people: PeopleLocalization
    val rooms: RoomsLocalization

    val technical: TechnicalLocalization
    val calculation: CalculationLocalization
}

interface ColumnsLocalization {
    val id: String
    val familyId: String
    val room: String
    val firstName: String
    val lastName: String
    val documentType: String
    val documentId: String
    val settlement: String
    val birthDate: String
    val sex: String
    val borderCrossDate: String
    val checkInDate: String
    val checkOutDate: String
    val phoneNumber: String
    val additionalInfo: String
    val ageAutomatic: String
    val ageCategoryAutomatic: String
}

interface OverviewLocalization {
    val sheetName: String
    val peopleCurrent: String
    val peopleTotal: String
    val byAge: String
    val nextAdultId: String
    val nextFamilyId: String
    val byAgeTotal: String

    val places: String
    val roomsQuantity: String
    val roomPlacesTotal: String
    val roomPlacesFilled: String
    val roomPlacesEmpty: String

    val total: String

    val sexWomen: String
    val sexMen: String
    val adults: String
    val children: String

    val ageStatsFrom: String
    val ageStatsTo: String
}

interface PeopleLocalization {
    val sheetName: String
    val peopleCurrentSheetName: String
}

interface RoomsLocalization {
    val sheetName: String
    val roomNumbers: String
    val placesQuantity: String
    val placesFilled: String
    val adults: String
    val teens: String
    val children: String
    val placesEmpty: String
    val comments: String
    val residentsInfoDoNotEdit: String
}

interface TechnicalLocalization {
    val sheetName: String

    val ageCategories: String
    val ageCategoriesDescr: String
    val ageCategoryAdult: String
    val ageCategoryAdultDescr: String
    val ageCategoryTeen: String
    val ageCategoryTeenDescr: String
    val ageCategoryChild: String
    val ageCategoryChildDescr: String

    val sex: String
    val sexMale: String
    val sexFemale: String

    val documentTypes: String

    val roomNumbers: String

    val sortBy: String

    val allKeyword: String
    val filteringSuffix: String

    val orderBy: String
    val ascending: String
    val descending: String
}

interface CalculationLocalization {
    val sheetName: String
    val filters: String
    val values: String
    val sortBy: String
    val orderBy: String

    val quantity: String

    val from: String
    val to: String
}
