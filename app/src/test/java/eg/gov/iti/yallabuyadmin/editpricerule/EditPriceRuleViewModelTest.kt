package eg.gov.iti.yallabuyadmin.editpricerule

import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeEditPriceRuleRepository
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
class EditPriceRuleViewModelTest {

    private lateinit var viewModel: EditPriceRuleViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditPriceRuleViewModel(FakeEditPriceRuleRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updatePriceRule should emit success and toast message`() = runTest {
        val rule = PriceRulesItem(title = "10% OFF")
        viewModel.updatePriceRule(id = 123L, rule = rule)
        advanceUntilIdle()

        val result = viewModel.updateState.value
        assertTrue(result is Response.Success)
        assertEquals("10% OFF", (result as Response.Success).data.title)

    }
}
