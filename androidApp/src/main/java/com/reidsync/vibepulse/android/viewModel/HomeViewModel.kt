package com.reidsync.vibepulse.android.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reidsync.vibepulse.android.VibePulseApplication
import com.reidsync.vibepulse.android.data.NotebookRepository
import com.reidsync.vibepulse.model.Journal
import com.reidsync.vibepulse.model.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * Created by Reid on 2023/12/12.
 * Copyright (c) 2023 Reid Byun. All rights reserved.
 */

class HomeViewModel(
	private val notebookRepository: NotebookRepository
): ViewModel() {
	private val _uiState = MutableStateFlow(HomeScreenUIState())
	val uiState: StateFlow<HomeScreenUIState> = _uiState.asStateFlow()
		.combine(notebookRepository.journals) { uiState, notebook ->
			uiState.copy(journals = notebook.journals)
		}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeScreenUIState())

	companion object {
		val Factory: ViewModelProvider.Factory = viewModelFactory {
			initializer {
				val application = (this[APPLICATION_KEY] as VibePulseApplication)
				HomeViewModel(
					notebookRepository = application.container.notebookRepository
				)
			}
		}
	}
}


data class HomeScreenUIState(
	//val journals: List<Journal> = emptyList()
	val journals: List<Journal> = listOf(
		Journal.mock, Journal.mock, Journal.mock,
		Journal.mock, Journal.mock, Journal.mock,
		Journal.mock, Journal.mock, Journal.mock,
		Journal.mock, Journal.mock, Journal.mock,
		Journal.mock, Journal.mock, Journal.mock,
		Journal.mock, Journal.mock, Journal.mock,
		Journal.mock, Journal.mock, Journal.mock
	)
)