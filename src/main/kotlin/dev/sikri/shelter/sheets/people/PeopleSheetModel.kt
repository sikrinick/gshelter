package dev.sikri.shelter.sheets.people

import io.github.sikrinick.geshikt.dsl.values.CellRangeReference

data class PeopleSheetBuilderModel(
    var id: CellRangeReference? = null,
    var familyId: CellRangeReference? = null,
    var room: CellRangeReference? = null,

    var firstName: CellRangeReference? = null,
    var lastName: CellRangeReference? = null,

    var documentType: CellRangeReference? = null,
    var documentId: CellRangeReference? = null,
    var pesel: CellRangeReference? = null,

    var region: CellRangeReference? = null,
    var city: CellRangeReference? = null,

    var birthdate: CellRangeReference? = null,
    var sex: CellRangeReference? = null,

    var borderCrossDate: CellRangeReference? = null,
    var checkInDate: CellRangeReference? = null,
    var checkOutDate: CellRangeReference? = null,

    var phoneNumber: CellRangeReference? = null,
    var remarks: CellRangeReference? = null,

    var age: CellRangeReference? = null,
    var ageCategories: CellRangeReference? = null
) {
    fun build() = PeopleSheetModel(
        id = id,
        familyId = familyId,
        room = room!!,
        firstName = firstName!!,
        lastName = lastName!!,
        documentType = documentType!!,
        documentId = documentId!!,
        pesel = pesel!!,
        region = region,
        city = city,
        birthdate = birthdate!!,
        sex = sex!!,
        borderCrossDate = borderCrossDate!!,
        checkInDate = checkInDate!!,
        checkOutDate = checkOutDate!!,
        phoneNumber = phoneNumber!!,
        remarks = remarks!!,
        age = age!!,
        ageCategories = ageCategories!!
    )
}

data class PeopleSheetModel(
    val id: CellRangeReference? = null,
    val familyId: CellRangeReference? = null,
    val room: CellRangeReference,

    val firstName: CellRangeReference,
    val lastName: CellRangeReference,

    val documentType: CellRangeReference,
    val documentId: CellRangeReference,
    val pesel: CellRangeReference,

    val region: CellRangeReference? = null,
    val city: CellRangeReference? = null,

    val birthdate: CellRangeReference,
    val sex: CellRangeReference,

    val borderCrossDate: CellRangeReference,
    val checkInDate: CellRangeReference,
    val checkOutDate: CellRangeReference,

    val phoneNumber: CellRangeReference,
    val remarks: CellRangeReference,

    val age: CellRangeReference,
    val ageCategories: CellRangeReference
)