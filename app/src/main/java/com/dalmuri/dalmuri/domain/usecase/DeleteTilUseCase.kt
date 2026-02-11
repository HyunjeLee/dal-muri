package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.repository.TilRepository
import javax.inject.Inject

class DeleteTilUseCase
    @Inject
    constructor(
        private val repository: TilRepository,
    ) {
        suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteTil(id)
    }
