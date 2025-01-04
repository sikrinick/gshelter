package dev.sikri.shelter.i18n

import dev.sikri.shelter.i18n.l10n.Localization
import dev.sikri.shelter.i18n.l10n.Polish
import dev.sikri.shelter.i18n.l10n.Ukrainian

sealed interface Legalization {
    val localization: Localization

    val asylumDocumentIdName: String

    val regionName: String
    val regions: List<String>

    val documentTypes: List<String>
}

data class UkrainianInPoland(
    override val localization: Localization,
) : Legalization {
    init {
        if (localization !in arrayOf(Polish, Ukrainian)) throw localizationError()
    }

    override val asylumDocumentIdName = when (localization) {
        Polish -> "PESEL"
        Ukrainian -> "PESEL"
        else -> throw localizationError()
    }

    override val regionName = when (localization) {
        Polish -> "Obwód lub AR"
        Ukrainian -> "Область або АР"
        else -> throw localizationError()
    }

    override val regions = when (localization) {
        Polish -> listOf(
            "Charkowski",
            "Chersoński",
            "Chmielnicki",
            "Czerkaski",
            "Czernihowski",
            "Czerniowiecki",
            "Dniepropetrowski",
            "Doniecki",
            "Iwanofrankowski",
            "Kijowski",
            "Kirowohradzki",
            "Lwowski",
            "Ługański",
            "Mikołajowski",
            "Odeski",
            "Połtawski",
            "Rówieński",
            "Sumski",
            "Tarnopolski",
            "Winnicki",
            "Wołyński",
            "Zakarpacki",
            "Zaporoski",
            "Żytomierski",
            "AR Krym"
        )
        Ukrainian -> listOf(
            "АР Крим",

            "Вінницька",
            "Волинська",

            "Дніпропетровська",
            "Донецька",

            "Житомирська",

            "Закарпатська",
            "Запорожська",

            "Івано-франківська",

            "Київська",
            "Кіровоградська",

            "Львівська",
            "Луганська",

            "Миколаївська",

            "Одеська",

            "Полтавська",

            "Рівненська",

            "Сумська",

            "Тернопільська",

            "Харківська",
            "Херсонська",
            "Хмельницька",

            "Черкаська",
            "Чернівецька",
            "Чернігівська",
        )
        else -> throw localizationError()
    }

    override val documentTypes = when (localization) {
        Polish -> listOf(
            "Paszport zagraniczny Ukrainy",
            "Paszport wewnętrzny Ukrainy",
            "ID karta Ukrainy",
            "Potwierdzenie narodzenia Ukrainy",
            "Paszport zagraniczny innego kraju",
            "Paszport wewnętrzny innego kraju",
            "ID karta innego kraju",
            "Potwierdzenie narodzenia innego kraju",
            "Inne (w uwagach)"
        )
        Ukrainian -> listOf(
            "Закордонний паспорт України",
            "Внутрішній Паспорт України",
            "Айді картка України",
            "Свідоцтво про народження України",
            "Закордоний паспорт іншої країни",
            "Внутрішній паспорт іншої країни",
            "Айді картка іншої країни",
            "Свідоцтво про народження іншої країни",
            "Інше (в коментарах)"
        )
        else -> throw localizationError()
    }

    private fun localizationError() =
        RuntimeException("${this::class.simpleName} supports only ${Polish::class.simpleName} and ${Ukrainian::class.simpleName} localization")
}

data object UkrainianInUkraine : Legalization {
    override val localization: Localization = Ukrainian

    override val asylumDocumentIdName = "ID"

    override val regionName = "Область або АР"

    override val regions = listOf(
        "АР Крим",

        "Вінницька",
        "Волинська",

        "Дніпропетровська",
        "Донецька",

        "Житомирська",

        "Закарпатська",
        "Запорожська",

        "Івано-франківська",

        "Київська",
        "Кіровоградська",

        "Львівська",
        "Луганська",

        "Миколаївська",

        "Одеська",

        "Полтавська",

        "Рівненська",

        "Сумська",

        "Тернопільська",

        "Харківська",
        "Херсонська",
        "Хмельницька",

        "Черкаська",
        "Чернівецька",
        "Чернігівська",
    )

    override val documentTypes = listOf(
        "ID картка",
        "Паспорт",
        "Інше"
    )
}

data object PolishInPoland : Legalization {
    override val localization: Localization = Polish

    override val asylumDocumentIdName = "PESEL"

    override val regionName =
        "Województwo"

    override val regions = listOf(
        "Dolnośląskie",
        "Kujawsko-pomorskie",
        "Lubelskie",
        "Lubuskie",
        "Łódzkie",
        "Małopolskie",
        "Mazowieckie",
        "Opolskie",
        "Podkarpackie",
        "Podlaskie",
        "Pomorskie",
        "Śląskie",
        "Świętokrzyskie",
        "Warmińsko-mazurskie",
        "Wielkopolskie",
        "Zachodniopomorskie",
    )

    override val documentTypes = listOf(
        "PESEL",
        "Inne"
    )
}