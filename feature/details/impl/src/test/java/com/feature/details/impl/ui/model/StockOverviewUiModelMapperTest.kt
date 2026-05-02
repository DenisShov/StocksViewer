package com.feature.details.impl.ui.model

import com.feature.details.impl.domain.model.Address
import com.feature.details.impl.domain.model.Branding
import com.feature.details.impl.domain.model.Company
import com.feature.details.impl.domain.model.StockOverview
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StockOverviewUiModelMapperTest {

    private fun overview(
        company: Company = company(),
    ) = StockOverview(
        requestId = "req-1",
        results = company,
        status = "OK",
    )

    private fun company(
        ticker: String = "AAPL",
        name: String = "Apple Inc.",
        marketCap: Double? = null,
        address: Address? = null,
        listDate: String? = null,
        branding: Branding? = null,
        description: String? = null,
    ) = Company(
        ticker = ticker,
        name = name,
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = "CS",
        active = true,
        marketCap = marketCap,
        address = address,
        listDate = listDate,
        branding = branding,
        description = description,
    )

    @Test
    fun toUiModel_mapsBasicFields() {
        val result = overview().toUiModel()
        assertEquals("AAPL", result.ticker)
        assertEquals("Apple Inc.", result.name)
        assertEquals("us", result.locale)
        assertEquals("Common Stock", result.type)
        assertEquals("XNAS", result.exchange)
    }

    @Test
    fun toUiModel_nullOptionalFields_mapToNull() {
        val result = overview().toUiModel()
        assertNull(result.marketCap)
        assertNull(result.description)
        assertNull(result.listDate)
        assertNull(result.iconUrl)
    }

    @Test
    fun toUiModel_fullAddress_joinsWithComma() {
        val address = Address("123 Main St", "Cupertino", "CA", "95014")
        val result = overview(company(address = address)).toUiModel()
        assertEquals("123 Main St, Cupertino, CA, 95014", result.address)
    }

    @Test
    fun toUiModel_partialAddress_skipsNulls() {
        val address = Address(address1 = "123 Main St", city = "Cupertino", state = null, postalCode = null)
        val result = overview(company(address = address)).toUiModel()
        assertEquals("123 Main St, Cupertino", result.address)
    }

    @Test
    fun toUiModel_nullAddress_returnsEmptyString() {
        val result = overview(company(address = null)).toUiModel()
        assertEquals("", result.address)
    }

    @Test
    fun toUiModel_allNullAddressFields_returnsEmptyString() {
        val address = Address(null, null, null, null)
        val result = overview(company(address = address)).toUiModel()
        assertEquals("", result.address)
    }

    @Test
    fun toUiModel_marketCapTrillions() {
        val result = overview(company(marketCap = 3_000_000_000_000.0)).toUiModel()
        assertEquals("3.00T", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapBillions() {
        val result = overview(company(marketCap = 110_590_000_000.0)).toUiModel()
        assertEquals("110.59B", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapMillions() {
        val result = overview(company(marketCap = 500_000_000.0)).toUiModel()
        assertEquals("500.00M", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapThousands() {
        val result = overview(company(marketCap = 50_000.0)).toUiModel()
        assertEquals("50.00K", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapSmall() {
        val result = overview(company(marketCap = 999.99)).toUiModel()
        assertEquals("999.99", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapNull() {
        val result = overview(company(marketCap = null)).toUiModel()
        assertNull(result.marketCap)
    }

    @Test
    fun toUiModel_marketCapExactBoundary_billion() {
        val result = overview(company(marketCap = 1_000_000_000.0)).toUiModel()
        assertEquals("1.00B", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapExactBoundary_million() {
        val result = overview(company(marketCap = 1_000_000.0)).toUiModel()
        assertEquals("1.00M", result.marketCap)
    }

    @Test
    fun toUiModel_marketCapExactBoundary_thousand() {
        val result = overview(company(marketCap = 1_000.0)).toUiModel()
        assertEquals("1.00K", result.marketCap)
    }

    @Test
    fun toUiModel_validDate_formatsCorrectly() {
        val result = overview(company(listDate = "1980-12-12")).toUiModel()
        assertEquals("12 December 1980", result.listDate)
    }

    @Test
    fun toUiModel_nullDate_returnsNull() {
        val result = overview(company(listDate = null)).toUiModel()
        assertNull(result.listDate)
    }

    @Test
    fun toUiModel_singleDigitDay_noLeadingZero() {
        val result = overview(company(listDate = "2020-01-05")).toUiModel()
        assertEquals("5 January 2020", result.listDate)
    }

    @Test
    fun toUiModel_brandingWithIconUrl_mapsIconUrl() {
        val branding = Branding(iconUrl = "https://example.com/icon.png")
        val result = overview(company(branding = branding)).toUiModel()
        assertEquals("https://example.com/icon.png", result.iconUrl)
    }

    @Test
    fun toUiModel_nullBranding_nullIconUrl() {
        val result = overview(company(branding = null)).toUiModel()
        assertNull(result.iconUrl)
    }

    @Test
    fun toUiModel_brandingWithNullIconUrl_nullIconUrl() {
        val branding = Branding(iconUrl = null)
        val result = overview(company(branding = branding)).toUiModel()
        assertNull(result.iconUrl)
    }
}
