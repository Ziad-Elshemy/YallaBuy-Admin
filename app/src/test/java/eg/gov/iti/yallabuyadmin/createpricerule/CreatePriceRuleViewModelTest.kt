package eg.gov.iti.yallabuyadmin.createpricerule

import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeCreatePriceRuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePriceRuleViewModelTest {

    private lateinit var viewModel: CreatePriceRuleViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreatePriceRuleViewModel(FakeCreatePriceRuleRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createPriceRule should emit success and toast message`() = runTest {
        // Arrange
        val testRule = PriceRulesItem(
            id = 123,
            title = "Test Rule",
            value = "-10",
            valueType = "fixed_amount",
            customerSelection = "all",
            targetType = "line_item",
            targetSelection = "all",
            allocationMethod = "across",
            startsAt = "2025-06-20T00:00:00Z",
            endsAt = "2025-06-30T00:00:00Z",
            usageLimit = "100",
            oncePerCustomer = false
        )

        // Act
        viewModel.createPriceRule(testRule)
        advanceUntilIdle()

        // Assert
        val result = viewModel.createState.first() { it !is Response.Loading }
        assertTrue(result is Response.Success)
        val data = (result as Response.Success).data
        assertEquals(testRule, data)
        assertEquals("Test Rule", data.title)
        assertThat(data.value , `is`("-10"))

    }
}
