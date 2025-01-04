package dev.sikri.shelter.i18n.l10n

data object Polish : Localization {
    override val columns = object : ColumnsLocalization {
        override val id = "Id"
        override val familyId = "Nr. rodziny"
        override val room = "Pokój"
        override val firstName = "Imię"
        override val lastName = "Nazwisko"
        override val documentType = "Rodzaj dokumentu"
        override val documentId = "Nr dokumentu"
        override val settlement = "Miejscowość"
        override val birthDate = "Data urodzenia"
        override val sex = "Płeć"
        override val borderCrossDate = "Data przekroczenia granicy"
        override val checkInDate = "Data zakwaterowania"
        override val checkOutDate = "Data wymeldowania"
        override val phoneNumber = "Nr telefonu"
        override val additionalInfo = "Uwagi"
        override val ageAutomatic = "Wiek (automat)"
        override val ageCategoryAutomatic = "Kategoria (automat)"
    }
    override val overview = object : OverviewLocalization {
        override val sheetName = "Ogólnie"
        override val peopleCurrent = "Ludzie"
        override val peopleTotal = "Za cały czas"
        override val byAge = "Za wiekiem"
        override val nextAdultId = "Następny ID dla dorosłych"
        override val nextFamilyId = "Następny Nr Rodziny"
        override val byAgeTotal = "Za wiekiem. Za cały czas"
        override val places = "Miejsca"
        override val roomsQuantity = "Ilość pokojów"
        override val roomPlacesTotal = "Ilość ogólna"
        override val roomPlacesFilled = "Zajętych"
        override val roomPlacesEmpty = "Wolnych"
        override val total = "Ogółem"
        override val sexWomen = "Kobiet"
        override val sexMen = "Mężczyzn"
        override val adults = "Dorosłych"
        override val children = "Dzieci"
        override val ageStatsFrom = "od, wiek"
        override val ageStatsTo = "do, wiek"
    }

    override val people = object : PeopleLocalization {
        override val sheetName = "Ludzie"
        override val peopleCurrentSheetName = "Techniczne. LudzieTeraz"
    }

    override val rooms = object : RoomsLocalization {
        override val sheetName = "Pokoje"
        override val roomNumbers = "Numery Pokojów"
        override val placesQuantity = "Ilość Miejsc"
        override val placesFilled = "Zajętych"
        override val adults = "Dorosłych"
        override val teens = "Nastolatków"
        override val children = "Dzieci"
        override val placesEmpty = "Wolnych"
        override val comments = "Komentarze do pokoju"
        override val residentsInfoDoNotEdit = "Uwagi obecnych mieszkańców (Nie edytować!)"
    }

    override val technical = object : TechnicalLocalization {
        override val sheetName = "Techniczne. Nie zmieniać"
        override val ageCategories = "Kategorie wiekowe"
        override val ageCategoriesDescr = "Opis kategorii wiekowych"
        override val ageCategoryAdult = "D"
        override val ageCategoryAdultDescr = "18+"
        override val ageCategoryTeen = "X"
        override val ageCategoryTeenDescr = "11-17"
        override val ageCategoryChild = "Z"
        override val ageCategoryChildDescr = "<=10"
        override val sex = "Płeć"
        override val sexMale = "M"
        override val sexFemale = "K"
        override val documentTypes = "Rodzaje dokumentów"
        override val roomNumbers = "Numery Pokojów"
        override val sortBy = "Sortowanie"
        override val allKeyword = "Wszystkie"
        override val filteringSuffix = "do filterowania"
        override val orderBy = "Uporządkowanie"
        override val ascending = "Rosnąco"
        override val descending = "Malejąco"
    }
    override val calculation = object : CalculationLocalization {
        override val sheetName = "Kalkulacje"
        override val filters = "Filtry"
        override val values = "Wartości"
        override val sortBy = "Sortować za"
        override val orderBy = "Uporządkować"

        override val quantity = "Ilość"

        override val from = "od"
        override val to = "do"
    }
}