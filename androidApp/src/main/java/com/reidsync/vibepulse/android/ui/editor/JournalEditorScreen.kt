package com.reidsync.vibepulse.android.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reidsync.vibepulse.android.AppThemeColor
import com.reidsync.vibepulse.android.data.conventions.toColor
import com.reidsync.vibepulse.android.ui.common.BaseToolbar
import com.reidsync.vibepulse.notebook.journal.Journal
import com.reidsync.vibepulse.util.format

/**
 * Created by Reid on 2023/12/18.
 * Copyright (c) 2023 Reid Byun. All rights reserved.
 */

@Composable
fun JournalEditorScreen(
	viewModel: JournalEditorViewModel,
	onNavigateUp: () -> Unit,
	onNavigateMetaEdit: (Journal) -> Unit
) {
	val uiState by viewModel.uiState.collectAsState()
	val focusManager = LocalFocusManager.current

	val clearFocus: (Boolean) -> Unit = {
		viewModel.setClearFocus(it)
	}

	LaunchedEffect (uiState.clearFocus) {
		focusManager.clearFocus()
		clearFocus(false)
	}

	Column(
		modifier = Modifier
			.background(AppThemeColor.current.vibePulseColors.background.toColor())
	) {
		EditorToolbar(
			journal = uiState.journal,
			onNavigateUp = onNavigateUp,
			onClearFocus = { clearFocus(true) },
			onNavigateMetaEdit = onNavigateMetaEdit
		)
		Editor(
			journal = uiState.journal,
			contents = uiState.contents,
			onContentsChange = { viewModel.editContents(it) },
			onClearFocus = { clearFocus(true) }
		)
	}
}

@Composable
fun Editor(
	journal: Journal,
	contents: String,
	onContentsChange: (String) -> Unit,
	onClearFocus: () -> Unit
) {

	Column(
		modifier = Modifier
			.fillMaxSize()
			.pointerInput(Unit) {
				detectTapGestures(onTap = {
					onClearFocus()
				})
			}
			.padding(15.dp),
	) {
		Text(
			text = journal.titleWithPlaceHolder,
			fontWeight = FontWeight.Bold,
			fontSize = 25.sp,
			color = AppThemeColor.current.vibePulseColors.vibeA.toColor()
		)

		OutlinedTextField(
			modifier = Modifier
				.clip(shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
				.background(AppThemeColor.current.vibePulseColors.listBackground.toColor())
				.fillMaxSize(),
			value = journal.contents,
			onValueChange = {
				//onValueChange(journal.copy(contents = it))
				onContentsChange(it)
			},
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = AppThemeColor.current.vibePulseColors.listBackground.toColor(),
				unfocusedBorderColor = AppThemeColor.current.vibePulseColors.listBackground.toColor(),
//				focusedContainerColor = Color.White,
//				unfocusedContainerColor = Color.White,
//				unfocusedTextColor = Color.Black,
//				focusedTextColor = Color.Black,
//				cursorColor = Color.Black
			),
			placeholder = {
				Text(
					text = journal.contentsWithPlaceHolder,
					color = Color.LightGray
				)
			},
			singleLine = false,
			keyboardOptions = KeyboardOptions.Default.copy(
				imeAction = ImeAction.None,
				keyboardType = KeyboardType.Text
			)
		)

	}
}

@Composable
fun EditorToolbar(
	journal: Journal,
	onNavigateUp: () -> Unit,
	onClearFocus: () -> Unit,
	onNavigateMetaEdit: (Journal) -> Unit
) {
	BaseToolbar(
		modifier = Modifier
			.fillMaxWidth()
			.pointerInput(Unit) {
				detectTapGestures(onTap = {
					onClearFocus()
				})
			}
			.padding(10.dp)
			.height(40.dp),
		title = {
			Row(
				modifier = it
			) {
				Icon(
					Icons.Filled.Create,
					contentDescription = "Weather",
					modifier = Modifier
						.size(23.dp)
						.align(Alignment.CenterVertically),
					tint = AppThemeColor.current.vibePulseColors.vibeD.toColor()
				)
				Spacer(modifier = Modifier.width(10.dp))
				Text(
					text = journal.date.format("MMMM d, yyyy"),
					fontWeight = FontWeight.Bold,
					fontSize = 23.sp,
					color = AppThemeColor.current.vibePulseColors.vibeD.toColor()
				)
			}

		},
		start = {
			Icon(
				Icons.Filled.KeyboardArrowLeft,
				contentDescription = "Back",
				modifier = it
					.size(40.dp)
					.clickable {
						onClearFocus()
						onNavigateUp()
					},
				tint = AppThemeColor.current.vibePulseColors.vibeB.toColor()
			)

		},
		end = {
			Icon(
				Icons.Filled.Settings,
				contentDescription = "Back",
				modifier = it
					.size(30.dp)
					.clickable {
						onClearFocus()
						onNavigateMetaEdit(journal)
					},
				tint = AppThemeColor.current.vibePulseColors.vibeB.toColor()
			)
		}
	)
}