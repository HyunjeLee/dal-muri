package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import javax.inject.Inject

class GetTilUseCase
    @Inject
    constructor(
        private val repository: TilRepository,
    ) {
        suspend operator fun invoke(id: Long): Result<Til> = repository.getTil(id)
    }
