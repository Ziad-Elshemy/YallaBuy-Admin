package eg.gov.iti.yallabuyadmin.coupons

import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeCouponsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CouponsViewModelTest {

    private lateinit var viewModel: CouponsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CouponsViewModel(FakeCouponsRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPriceRules should emit success`() = runTest {
        viewModel.fetchPriceRules()
        advanceUntilIdle()

        val result = viewModel.priceRules.value
        assertTrue(result is Response.Success)
        assertEquals(1, (result as Response.Success).data.size)
    }

    @Test
    fun `fetchDiscountCodes should emit success`() = runTest {
        viewModel.fetchDiscountCodes()
        advanceUntilIdle()

        val result = viewModel.discountCodes.value
        assertTrue(result is Response.Success)
        assertEquals("SUMMER", (result as Response.Success).data.first().code)
    }

//    @Test
//    fun `deletePriceRule should emit success message`() = runTest {
//        viewModel.deletePriceRule(1L)
//        val message = withTimeoutOrNull(1000) { viewModel.deleteResult.first() }
//
//        assertEquals("Rule deleted successfully", message)
//    }
//
////    @Test
//    fun `deleteDiscountCode should emit success message`() = runTest {
//        viewModel.deleteDiscountCode(1L, 1L)
//        val message = withTimeoutOrNull(1000) { viewModel.deleteResult.first() }
//
//        assertEquals("Discount deleted successfully", message)
//    }
//
//    @Test
//    fun `updateDiscountCode should emit updated message`() = runTest {
//        viewModel.updateDiscountCode(1L, 1L, "WINTER")
//        val message = withTimeoutOrNull(1000) { viewModel.toastMessage.first() }
//
//        assertEquals("Discount WINTER updated", message)
//    }
}
