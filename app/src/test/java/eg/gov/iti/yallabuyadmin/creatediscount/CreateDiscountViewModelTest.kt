package eg.gov.iti.yallabuyadmin.creatediscount

import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeCreateDiscountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateDiscountViewModelTest {

    private lateinit var viewModel: CreateDiscountViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreateDiscountViewModel(FakeCreateDiscountRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPriceRules should emit list of rules`() = runTest {
        viewModel.fetchPriceRules()
        advanceUntilIdle()

        val result = viewModel.priceRules.value
        assertTrue(result is Response.Success)
        assertEquals(2, (result as Response.Success).data.size)
        assertEquals("Summer Sale", result.data[0].title)
    }

    @Test
    fun `createDiscountCode should emit success and toast`() = runTest {
        val discount = DiscountCode(code = "NEW10", priceRuleId = 1L)

        viewModel.createDiscountCode(1L, discount)
        advanceUntilIdle()

        val result = viewModel.createState.value
        assertTrue(result is Response.Success)
        assertEquals("NEW10", (result as Response.Success).data.code)
        
    }
}
