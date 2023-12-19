package com.reidsync.vibepulse.android.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reidsync.vibepulse.android.VibePulseApplication
import com.reidsync.vibepulse.android.data.NotebookRepository
import com.reidsync.vibepulse.model.Journal
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by Reid on 2023/12/18.
 * Copyright (c) 2023 Reid Byun. All rights reserved.
 */

class JournalEditorViewModel(
	val journal: Journal,
	private val notebookRepository: NotebookRepository
): ViewModel() {
	private val _uiState = MutableStateFlow(JournalEditorUIState())
	val uiState: StateFlow<JournalEditorUIState> = _uiState.asStateFlow()
		.combine(notebookRepository.notebook) { uiState, notebook ->
			uiState.copy(journal = notebook.journals.first { it.id == uiState.journal.id })
		}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), JournalEditorUIState())

	private val _saveJournalState = MutableStateFlow<Journal?>(null)
	private val saveJournalState: StateFlow<Journal?> = _saveJournalState.asStateFlow()

	companion object {
		fun Factory(
			journal: Journal
		): ViewModelProvider.Factory = viewModelFactory {
			initializer {
				val application = (this[APPLICATION_KEY] as VibePulseApplication)
				JournalEditorViewModel(
					journal = journal,
					notebookRepository = application.container.notebookRepository)
			}
		}
	}

	init {
		_uiState.update {
			it.copy(
				journal = journal,
				contents = journal.contents
			)
		}

		viewModelScope.launch {
			val saveInterval : Long = 100
			@OptIn(FlowPreview::class)
			saveJournalState.debounce(saveInterval).collect { state ->
				if( state != null ) {
					_saveJournalState.getAndUpdate { null }
						?.let {
							notebookRepository.update(it)
						}
				}
			}
		}
	}

	override fun onCleared() {
		_saveJournalState.getAndUpdate { null }
			?.let {
				runBlocking { notebookRepository.update(it) }
			}
		super.onCleared()
	}

	fun editJournal(journal: Journal) {		//var toSave : Journal? = null
		_uiState.updateAndGet {
			it.copy(journal = journal)
		}.also {
			saveJournal(it.journal)
		}
	}

	fun editContents(contents: String) {
		_uiState.updateAndGet {
			it.copy(contents = contents)
		}.also {
			editJournal(it.journal.copy(contents = contents))
		}
	}

	private fun saveJournal(journal: Journal) {
		_saveJournalState.update { journal }
	}

	fun setClearFocus(on: Boolean) {
		_uiState.update {
			it.copy(clearFocus = on)
		}
	}
}

data class JournalEditorUIState(
	val journal: Journal = Journal(),
	val clearFocus: Boolean = false,
	val contents: String = ""
) {
	val title = journal.title
}

