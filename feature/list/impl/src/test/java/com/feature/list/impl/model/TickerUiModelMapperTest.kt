package com.feature.list.impl.model

import com.core.model.Ticker
import org.junit.Test
import kotlin.test.assertEquals

class TickerUiModelMapperTest {

    private fun ticker(type: String = "CS") = Ticker(
        ticker = "AAPL",
        name = "Apple Inc.",
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = type,
        active = true,
    )

    @Test
    fun toUiModel_mapsTickerAndName() {
        val result = ticker().toUiModel()
        assertEquals("AAPL", result.ticker)
        assertEquals("Apple Inc.", result.name)
    }

    @Test
    fun toUiModel_mapsCS_toCommonStock() {
        val result = ticker(type = "CS").toUiModel()
        assertEquals("Common Stock", result.type)
    }

    @Test
    fun toUiModel_mapsETF_toExchangeTradedFund() {
        val result = ticker(type = "ETF").toUiModel()
        assertEquals("Exchange Traded Fund", result.type)
    }

    @Test
    fun toUiModel_mapsADRC_toDepositaryReceipt() {
        val result = ticker(type = "ADRC").toUiModel()
        assertEquals("Depositary Receipt", result.type)
    }

    @Test
    fun toUiModel_unknownType_passesThrough() {
        val result = ticker(type = "WARRANT").toUiModel()
        assertEquals("WARRANT", result.type)
    }
}
