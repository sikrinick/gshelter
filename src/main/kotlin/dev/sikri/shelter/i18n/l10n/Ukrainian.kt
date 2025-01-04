package dev.sikri.shelter.i18n.l10n

data object Ukrainian : Localization {
    override val columns = object : ColumnsLocalization {
        override val id = "Айді"
        override val familyId = "Нр. родини"
        override val room = "Нр. кімнати"
        override val firstName = "Імʼя"
        override val lastName = "Прізвище"
        override val documentType = "Тип документу"
        override val documentId = "Номер документу"
        override val settlement = "Населений пункт"
        override val birthDate = "Дата народження"
        override val sex = "Стать"
        override val borderCrossDate = "Дата перетину кордону"
        override val checkInDate = "Дата заселення"
        override val checkOutDate = "Дата виселення"
        override val phoneNumber = "Номер телефону"
        override val additionalInfo = "Коментар"
        override val ageAutomatic = "Вік (Автомат)"
        override val ageCategoryAutomatic = "Категорія (Автомат)"

    }
    override val overview = object : OverviewLocalization {
        override val sheetName = "Загальне"
        override val peopleCurrent = "Людей"
        override val peopleTotal = "За весь час"
        override val byAge = "За віком"
        override val nextAdultId = "Наступний Айді для дорослих"
        override val nextFamilyId = "Наступний Айді родини"
        override val byAgeTotal = "За віком. За весь час"
        override val places = "Кімнати і місця"
        override val roomsQuantity = "Кількість кімнат"
        override val roomPlacesTotal = "Кількість загальна"
        override val roomPlacesFilled = "Зайнятих"
        override val roomPlacesEmpty = "Вільних"
        override val total = "Всього"
        override val sexWomen = "Жінок"
        override val sexMen = "Чоловіків"
        override val adults = "Дорослих"
        override val children = "Дітей"
        override val ageStatsFrom = "від, вік"
        override val ageStatsTo = "до, вік"

    }
    override val people = object : PeopleLocalization {
        override val sheetName = "Люди"
        override val peopleCurrentSheetName = "Технічне. ЛюдиЗараз"

    }
    override val rooms = object : RoomsLocalization {
        override val sheetName = "Кімнати"
        override val roomNumbers = "Номери Кімнат"
        override val placesQuantity = "Кількість місць"
        override val placesFilled = "Зайнятих"
        override val adults = "Дорослих"
        override val teens = "Підлітків"
        override val children = "Дітей"
        override val placesEmpty = "Пустих"
        override val comments = "Коментарі до кімнати"
        override val residentsInfoDoNotEdit = "Коментарі до мешканців (не змінювати!)"

    }
    override val technical = object : TechnicalLocalization {
        override val sheetName = "Технічне. Не змінювати"
        override val ageCategories = "Вікові категорії"
        override val ageCategoriesDescr = "Опис вікових категорій"
        override val ageCategoryAdult = "Д"
        override val ageCategoryAdultDescr = "18+. Дорослі"
        override val ageCategoryTeen = "П"
        override val ageCategoryTeenDescr = "11-17. Підлітки"
        override val ageCategoryChild = "М"
        override val ageCategoryChildDescr = "<=10. Малолітні"
        override val sex = "Стать"
        override val sexMale = "Ч"
        override val sexFemale = "Ж"
        override val documentTypes = "Види документів"
        override val roomNumbers = "Номери Кімнат"
        override val sortBy = "Сортування"
        override val allKeyword = "Всі"
        override val filteringSuffix = "до фільтрування"
        override val orderBy = "Порядок"
        override val ascending = "За зростанням"
        override val descending = "За спаданням"

    }
    override val calculation = object : CalculationLocalization {
        override val sheetName = "Калькуляції"
        override val filters = "Фільтри"
        override val values = "Значення"
        override val sortBy = "Сортувати за"
        override val orderBy = "Порядок"
        override val quantity = "Кількість"
        override val from = "від"
        override val to = "до"

    }
}