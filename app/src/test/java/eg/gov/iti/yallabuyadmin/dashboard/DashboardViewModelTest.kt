package eg.gov.iti.yallabuyadmin.dashboard

import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.repo.FakeDashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DashboardViewModel(FakeDashboardRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchDashboardData should emit correct counts`() = runTest {
        // When
        viewModel.fetchDashboardData()

        // Then
        val result = viewModel.dashboardData.first { it !is Response.Loading }

        assertThat(result is Response.Success, `is`(true))

        val data = (result as Response.Success).data
        assertThat(data.productCount, `is`(2))
        assertThat(data.priceRuleCount, `is`(2))
        assertThat(data.discountCount, `is`(3))
        assertThat(data.vendorsCount, `is`(2))
        assertThat(data.inventoryItemsCount, `is`(2))
    }
}
