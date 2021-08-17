package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.managePoints.repo.ManagePointsFragmentRepo
import com.onelink.nrlp.android.features.managePoints.viewmodel.ManagePointsFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class ManagePointsModelTest : BaseViewModelTest() {

    private lateinit var viewModel: ManagePointsFragmentViewModel
    private lateinit var managePointsRepo: ManagePointsFragmentRepo


    override fun setUp() {
        super.setUp()
        managePointsRepo = Mockito.mock(ManagePointsFragmentRepo::class.java)
        viewModel = ManagePointsFragmentViewModel(managePointsRepo)
    }

    @Test
    fun validationPassedSuccess() {


        assertTrue(viewModel.validationPointsPassed.value != false)

        assertTrue(viewModel.validationSelectedBenePassed.value != false)

        assertTrue(viewModel.points.value.isNullOrEmpty())

        assertNull(viewModel.selectedBene.value)

        viewModel.validationPointsPassed.value = false

        viewModel.validationSelectedBenePassed.value = false

        val isPassed = viewModel.validationsPassed(
            1,
            "2500"
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationPointsPassed.value)

        assertTrue(viewModel.validationPointsPassed.value!!)

        assertNotNull(viewModel.validationSelectedBenePassed.value)

        assertTrue(viewModel.validationSelectedBenePassed.value!!)

        assertTrue(
            viewModel.validationSelectedBenePassed.value!!
                    && viewModel.validationPointsPassed.value!!
        )

    }


    @Test
    fun validationPassedFailed() {

        assertFalse(viewModel.validationPointsPassed.value == false)

        assertFalse(viewModel.validationSelectedBenePassed.value == false)


        val isPassed = viewModel.validationsPassed(
            1,
            ""
        )

        assertFalse(isPassed)

    }


}