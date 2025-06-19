package eg.gov.iti.yallabuyadmin.addproduct

import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeCreateProductRepository
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
class CreateProductViewModelTest {

    private lateinit var viewModel: CreateProductViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreateProductViewModel(FakeCreateProductRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllVendors should return distinct list of vendor names`() = runTest {
        viewModel.loadInitialData()
        advanceUntilIdle()

        val result = viewModel.vendors.value
        assertTrue(result is Response.Success)
        val vendors = (result as Response.Success).data

        assertEquals(listOf("Nike", "Adidas"), vendors)
    }

    @Test
    fun `getAllProductTypes should return distinct list of product types`() = runTest {
        viewModel.loadInitialData()
        advanceUntilIdle()

        val result = viewModel.productTypes.value
        assertTrue(result is Response.Success)
        val types = (result as Response.Success).data

        assertEquals(listOf("Shoes", "T-Shirts"), types)
    }

    @Test
    fun `createProduct should emit success and show toast messages`() = runTest {
        val product = ProductsItem(title = "Running Shoes")
        viewModel.createProduct(product, selectedCollectionId = 123L)
        advanceUntilIdle()

        val result = viewModel.createState.value
        assertTrue(result is Response.Success)
        assertEquals("Running Shoes", (result as Response.Success).data.title)

    }
}
