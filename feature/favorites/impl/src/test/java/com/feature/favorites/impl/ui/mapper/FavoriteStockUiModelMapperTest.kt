package com.feature.favorites.impl.ui.mapper

import com.sharedlibrary.favorites.domain.model.FavoriteStock
import org.junit.Test
import kotlin.test.assertEquals

class FavoriteStockUiModelMapperTest {

    private fun favorite(type: String = "CS") = FavoriteStock(
        ticker = "AAPL",
        name = "Apple Inc.",
        type = type,
        primaryExchange = "XNAS",
    )

    @Test
    fun toUiModel_mapsTicker() {
        val result = favorite().toUiModel()
        assertEquals("AAPL", result.ticker)
    }

    @Test
    fun toUiModel_mapsName() {
        val result = favorite().toUiModel()
        assertEquals("Apple Inc.", result.name)
    }

    @Test
    fun toUiModel_mapsCS_toCommonStock() {
        val result = favorite(type = "CS").toUiModel()
        assertEquals("Common Stock", result.type)
    }

    @Test
    fun toUiModel_mapsETF_toExchangeTradedFund() {
        val result = favorite(type = "ETF").toUiModel()
        assertEquals("Exchange Traded Fund", result.type)
    }

    @Test
    fun toUiModel_mapsADRC_toDepositaryReceipt() {
        val result = favorite(type = "ADRC").toUiModel()
        assertEquals("Depositary Receipt", result.type)
    }

    @Test
    fun toUiModel_unknownType_passesThrough() {
        val result = favorite(type = "WARRANT").toUiModel()
        assertEquals("WARRANT", result.type)
    }

    @Test
    fun toUiModel_emptyType_passesThrough() {
        val result = favorite(type = "").toUiModel()
        assertEquals("", result.type)
    }
}
