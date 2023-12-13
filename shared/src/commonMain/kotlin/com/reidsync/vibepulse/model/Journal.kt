package com.reidsync.vibepulse.model

import com.reidsync.vibepulse.primitives.uuid.UUID
import com.reidsync.vibepulse.primitives.uuid.randomUUID
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Created by Reid on 2023/12/12.
 * Copyright (c) 2023 Reid Byun. All rights reserved.
 */

data class Journal(
	val id: UUID,
	val title: String,
	val date: LocalDateTime,
	val contents: String
) {
	companion object
}

val Journal.Companion.mock: Journal
	get() = Journal(
		id = UUID.randomUUID(),
		title = "Test title",
		date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
		contents = """
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor \
          incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud \
          exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure \
          dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \
          Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt \
          mollit anim id est laborum.
          """
	)