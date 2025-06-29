package eg.gov.iti.yallabuyadmin.products

import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeProductsRepository
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
class ProductsViewModelTest {

    private lateinit var viewModel: ProductsViewModel
    private lateinit var fakeRepo: FakeProductsRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeProductsRepository()
        viewModel = ProductsViewModel(fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchProductsItems should emit product list`() = runTest {
        // When
        viewModel.fetchProductsItems()

        // Then
        val state = viewModel.allProducts.first { it !is Response.Loading }
        assertTrue(state is Response.Success)
        assertEquals(2, (state as Response.Success).data?.size)

    }

    @Test
    fun `deleteProductById should remove product and emit updated list`() = runTest {
        // Arrange
        viewModel.fetchProductsItems()

        val initial = viewModel.allProducts.first { it !is Response.Loading }
        assertTrue(initial is Response.Success)
        assertEquals(2, (initial as Response.Success).data?.size)

        // Act
        viewModel.deleteProductById(1L)

        val updated = viewModel.allProducts.first { it !is Response.Loading }

        // Assert
        assertTrue(updated is Response.Success)
        assertEquals(1, (updated as Response.Success).data?.size)
        assertEquals(2L, updated.data?.get(0)?.id)
    }

}
