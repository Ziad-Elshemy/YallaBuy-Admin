package eg.gov.iti.yallabuyadmin.inventory

import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeInventoryRepository
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
class InventoryViewModelTest {

    private lateinit var viewModel: InventoryViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = InventoryViewModel(FakeInventoryRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchInventoryItems should emit items`() = runTest {
        viewModel.fetchInventoryItems()

        val result = viewModel.inventoryItems.first { it !is Response.Loading }

        assertTrue(result is Response.Success)
        val items = (result as Response.Success).data

        assertEquals(2, items.size)
        assertEquals("T-Shirt", items[0].title)
    }

    @Test
    fun `updateVariantQuantity should update item quantity and emit message`() = runTest {
        viewModel.fetchInventoryItems()

        viewModel.updateVariantQuantity(inventoryItemId = 1L, newQuantity = 10)

        val updated = viewModel.inventoryItems.first { it is Response.Success } as Response.Success
        val item = updated.data.first { it.inventoryItemId == 1L }

        assertEquals(10, item.quantity)

    }
}
