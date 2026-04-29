package com.feature.details.impl.domain.mapper

import com.core.network.model.stocksDetails.AddressResponse
import com.core.network.model.stocksDetails.BrandingResponse
import com.core.network.model.stocksDetails.CompanyResponse
import com.core.network.model.stocksDetails.StockOverviewResponse
import com.feature.details.impl.data.mapper.toDomain
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StockOverviewMapperTest {

    private val companyResponse = CompanyResponse(
        ticker = "AAPL",
        name = "Apple Inc.",
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = "CS",
        active = true,
        currencyName = "usd",
        cik = "0000320193",
        marketCap = 3_000_000_000_000.0,
        totalEmployees = 164000,
        description = "Apple designs and sells electronics.",
        sicDescription = "Electronic Computers",
        homepageUrl = "https://www.apple.com",
        listDate = "1980-12-12",
        address = AddressResponse("One Apple Park Way", "Cupertino", "CA", "95014"),
        branding = BrandingResponse(
            logoUrl = "https://example.com/logo.png",
            iconUrl = "https://example.com/icon.png",
        ),
    )

    private val overviewResponse = StockOverviewResponse(
        requestId = "req-1",
        results = companyResponse,
        status = "OK",
    )

    @Test
    fun stockOverviewResponse_toDomain_mapsTopLevelFields() {
        val result = overviewResponse.toDomain()
        assertEquals("req-1", result.requestId)
        assertEquals("OK", result.status)
        assertEquals("AAPL", result.results.ticker)
    }

    @Test
    fun companyResponse_toDomain_mapsRequiredFields() {
        val result = companyResponse.toDomain()
        assertEquals("AAPL", result.ticker)
        assertEquals("Apple Inc.", result.name)
        assertEquals("stocks", result.market)
        assertEquals("us", result.locale)
        assertEquals("XNAS", result.primaryExchange)
        assertEquals("CS", result.type)
        assertEquals(true, result.active)
    }

    @Test
    fun companyResponse_toDomain_mapsOptionalFields() {
        val result = companyResponse.toDomain()
        assertEquals("usd", result.currencyName)
        assertEquals("0000320193", result.cik)
        assertEquals(3_000_000_000_000.0, result.marketCap)
        assertEquals(164000, result.totalEmployees)
        assertEquals("Apple designs and sells electronics.", result.description)
        assertEquals("Electronic Computers", result.sicDescription)
        assertEquals("https://www.apple.com", result.homepageUrl)
        assertEquals("1980-12-12", result.listDate)
    }

    @Test
    fun companyResponse_toDomain_nullOptionalFields() {
        val response = companyResponse.copy(
            currencyName = null,
            cik = null,
            marketCap = null,
            totalEmployees = null,
            description = null,
            address = null,
            branding = null,
        )
        val result = response.toDomain()
        assertNull(result.currencyName)
        assertNull(result.cik)
        assertNull(result.marketCap)
        assertNull(result.totalEmployees)
        assertNull(result.description)
        assertNull(result.address)
        assertNull(result.branding)
    }

    @Test
    fun addressResponse_toDomain_mapsAllFields() {
        val address = AddressResponse("123 Main St", "Cupertino", "CA", "95014")
        val result = address.toDomain()
        assertEquals("123 Main St", result.address1)
        assertEquals("Cupertino", result.city)
        assertEquals("CA", result.state)
        assertEquals("95014", result.postalCode)
    }

    @Test
    fun addressResponse_toDomain_nullFields() {
        val address = AddressResponse(null, null, null, null)
        val result = address.toDomain()
        assertNull(result.address1)
        assertNull(result.city)
        assertNull(result.state)
        assertNull(result.postalCode)
    }

    @Test
    fun brandingResponse_toDomain_appendsApiKey() {
        val branding = BrandingResponse(
            logoUrl = "https://example.com/logo.png",
            iconUrl = "https://example.com/icon.png",
        )
        val result = branding.toDomain()
        assertNotNull(result.logoUrl)
        assertNotNull(result.iconUrl)
        assertTrue(result.logoUrl!!.contains("apiKey="))
        assertTrue(result.iconUrl!!.contains("apiKey="))
        assertTrue(result.logoUrl!!.startsWith("https://example.com/logo.png?apiKey="))
        assertTrue(result.iconUrl!!.startsWith("https://example.com/icon.png?apiKey="))
    }

    @Test
    fun brandingResponse_toDomain_nullUrls() {
        val branding = BrandingResponse(logoUrl = null, iconUrl = null)
        val result = branding.toDomain()
        assertNull(result.logoUrl)
        assertNull(result.iconUrl)
    }
}
