package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTilsUseCase
    @Inject
    constructor(
        private val tilRepository: TilRepository,
    ) {
        operator fun invoke(): Flow<List<Til>> = tilRepository.getAllTils()
    }
