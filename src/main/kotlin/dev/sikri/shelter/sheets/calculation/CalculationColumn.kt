package dev.sikri.shelter.sheets.calculation

import io.github.sikrinick.geshikt.dsl.component.CellFormat
import io.github.sikrinick.geshikt.dsl.component.HasColumns
import io.github.sikrinick.geshikt.dsl.component.modifiers.Modifier
import io.github.sikrinick.geshikt.dsl.component.modifiers.allBorders
import io.github.sikrinick.geshikt.dsl.component.modifiers.validateOneOf
import io.github.sikrinick.geshikt.dsl.component.style.text.bold
import io.github.sikrinick.geshikt.dsl.types.HasFormulas
import io.github.sikrinick.geshikt.dsl.types.Type
import io.github.sikrinick.geshikt.dsl.utils.asSheetRefName
import io.github.sikrinick.geshikt.dsl.values.CellRangeReference
import io.github.sikrinick.geshikt.dsl.values.CellReference
import io.github.sikrinick.geshikt.dsl.values.invoke
import io.github.sikrinick.geshikt.dsl.values.lazyCell
import dev.sikri.shelter.sheets.Technical

interface CalculationColumn {
    enum class Operator(val op: String) {
        EQ("="),
        GT(">"),
        GE(">="),
        LT("<"),
        LE("<=")
    }
    fun buildForCalculation(sheetName: String, colIdx: Int, technical: Technical): HasColumns.() -> List<CellReference>
}

class DefaultCalculationColumn(
    private val header: String,
    private val format: CellFormat,
    private val operator: CalculationColumn.Operator,
    private val filteringValues: CellRangeReference? = null
) : CalculationColumn {
    override fun buildForCalculation(sheetName: String, colIdx: Int, technical: Technical): HasColumns.() -> List<CellReference> = {
        val valueNamed = "${header}SqlCondition".asSheetRefName(sheetName)
        val queryNamed = "${header}SqlQuery".asSheetRefName(sheetName)
        val valueCell = lazyCell(
            modifier = filteringValues?.let { Modifier.validateOneOf(it) } ?: Modifier.None,
            named = valueNamed, cellFormat = format
        )
        val query = lazyCell(named = queryNamed) {
            val ref = valueCell.reference
            val a1column = "Col${colIdx + 1}"
            join(
                " ",
                a1column.type(),
                `if`(
                    isblank(ref) or (ref isEqualTo technical.allKeyword),
                    "= $a1column",
                    escaped(operator, ref, format)
                )
            )
        }
        column(Modifier.allBorders()) {
            cell(header, Modifier.bold(true))
            valueCell()
            query()
        }
        listOf(query.reference)
    }
}

class ComparableCalculationColumnWithEmptyByDefault(
    private val header: String,
    private val format: CellFormat,
    private val filteringValues: CellRangeReference? = null
) : CalculationColumn {
    override fun buildForCalculation(sheetName: String, colIdx: Int, technical: Technical): HasColumns.() -> List<CellReference> = {
        val fromHeader = "${header}, od"
        val toHeader = "${header}, do"

        val fromValueNamed = "${fromHeader}SqlCondition".asSheetRefName(sheetName)
        val toValueNamed = "${toHeader}SqlCondition".asSheetRefName(sheetName)
        val fromQueryNamed = "${fromHeader}SqlQuery".asSheetRefName(sheetName)
        val toQueryNamed = "${toHeader}SqlQuery".asSheetRefName(sheetName)

        val fromValueCell = lazyCell(
            modifier = filteringValues?.let { Modifier.validateOneOf(it) } ?: Modifier.None,
            named = fromValueNamed, cellFormat = format
        )
        val toValueCell = lazyCell(
            modifier = filteringValues?.let { Modifier.validateOneOf(it) } ?: Modifier.None,
            named = toValueNamed, cellFormat = format
        )
        val a1column = "Col${colIdx + 1}"

        val fromQuery = lazyCell(named = fromQueryNamed) {
            join(
                " ",
                a1column.type(),
                `if`(
                    condition = (
                        isblank(fromValueCell.reference) or (fromValueCell.reference isEqualTo technical.allKeyword)
                    ) and (
                        isblank(toValueCell.reference) or (toValueCell.reference isEqualTo technical.allKeyword)
                    ),
                    ifTrue = "is null",
                    ifFalse = `if`(
                        isblank(fromValueCell.reference) or (fromValueCell.reference isEqualTo technical.allKeyword),
                        "= $a1column",
                        escaped(CalculationColumn.Operator.GE, fromValueCell.reference, format)
                    )
                )
            )
        }
        val toQuery = lazyCell(named = toQueryNamed) {
            join(
                " ",
                a1column.type(),
                `if`(
                    condition = (
                        isblank(fromValueCell.reference) or (fromValueCell.reference isEqualTo technical.allKeyword)
                    ) and (
                        isblank(toValueCell.reference) or (toValueCell.reference isEqualTo technical.allKeyword)
                    ),
                    ifTrue = "is null",
                    ifFalse = `if`(
                        isblank(toValueCell.reference) or (toValueCell.reference isEqualTo technical.allKeyword),
                        "= $a1column",
                        escaped(CalculationColumn.Operator.LE, toValueCell.reference, format)
                    )
                )
            )
        }
        column(Modifier.allBorders()) {
            cell(fromHeader, Modifier.bold(true))
            fromValueCell()
            fromQuery()
        }
        column(Modifier.allBorders()) {
            cell(toHeader, Modifier.bold(true))
            toValueCell()
            toQuery()
        }
        listOf(fromQuery.reference, toQuery.reference)
    }
}


class MultipleCalculationColumns(
    private vararg val calculationColumns: CalculationColumn
) : CalculationColumn {
    override fun buildForCalculation(sheetName: String, colIdx: Int, technical: Technical): HasColumns.() -> List<CellReference> = {
        calculationColumns.flatMap { it.buildForCalculation(sheetName, colIdx, technical).invoke(this) }
    }
}


private fun HasFormulas.escaped(
    operator: CalculationColumn.Operator,
    cellReference: CellReference,
    cellFormat: CellFormat
): Type.Text = when (cellFormat) {
    CellFormat.Text, CellFormat.Automatic ->
        join("",
            operator.op.type(),
            " ".type(),
            char(34), cellReference.type(), char(34)
        )
    CellFormat.Number ->
        join(" ", operator.op.type(), cellReference.type())
    is CellFormat.Date ->
        join("",
            operator.op.type(),
            " ".type(), "date".type(), " ".type(),
            char(34),
            text(
                cellReference,
                "yyyy-mm-dd",
            ),
            char(34)
        )
}
