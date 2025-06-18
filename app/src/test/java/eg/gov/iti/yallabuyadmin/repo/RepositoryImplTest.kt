package eg.gov.iti.yallabuyadmin.repo

import eg.gov.iti.yallabuyadmin.model.Image
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.network.FakeLocalDataSource
import eg.gov.iti.yallabuyadmin.network.FakeRemoteDataSource
import eg.iti.mad.climaguard.repo.Repository
import eg.iti.mad.climaguard.repo.RepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RepositoryImplTest{

    private lateinit var repository: RepositoryImpl

    @Before
    fun setup(){
        val fakeProducts = listOf(
            ProductsItem(
                id = 12345L,
                title = "Test Product",
                vendor = "YallaBuy",
                productType = "Shoes",
                image = Image(src = "https://via.placeholder.com/150")
            )
        )

        val fakeResponse = ProductsResponse(products = fakeProducts)
        val fakeRemote = FakeRemoteDataSource(fakeResponse)
        repository = RepositoryImpl(FakeLocalDataSource(),fakeRemote)

    }

    @Test
    fun `getAllProducts should return list of products`() = runTest {
        // When
        val result = repository.getAllProducts().first()

        // Then
        assertThat(result.products?.size, `is`(1))
        assertThat(result.products?.get(0)?.title, `is`("Test Product"))
        assertThat(result.products?.get(0)?.vendor, `is`("YallaBuy"))
    }

    @Test
    fun getAllDiscountCodes_returnsCorrectlyMappedList() = runTest {
        val result = repository.getAllDiscountCodes().first()

        // should have 3 items >> 2 (from rule 1) , 1 (from rule 2)
        assertThat(result.size, `is`(3))
        assertThat(result[0].code, `is`("SAVE10"))
        assertThat(result[1].priceRuleId, `is`(1L))
        assertThat(result[2].code, `is`("HELLO5"))
    }


    @Test
    fun updateProduct_returnsUpdatedProductCorrectly() = runTest {
        // Given
        val testProduct = ProductsItem(
            id = 123L,
            title = "Updated Title",
            bodyHtml = "Updated Description"
        )

        val updateRequest = UpdateProductRequest(product = testProduct)

        // When
        val result = repository.updateProduct(123L, updateRequest).first()

        // Then
        assertThat(result, `is`(testProduct))
        assertThat(result?.title, `is`("Updated Title"))
    }



}