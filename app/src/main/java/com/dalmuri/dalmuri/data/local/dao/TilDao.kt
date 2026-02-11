package com.dalmuri.dalmuri.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dalmuri.dalmuri.data.local.entity.TilEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TilDao {
    // TIL 작성 및 분석 결과 업데이트
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTil(til: TilEntity): Long

    // 날짜 역순(최신순)으로 모든 TIL 가져오기
    @Query("SELECT * FROM til_items ORDER BY createdAt DESC")
    fun getAllTils(): Flow<List<TilEntity>>

    // 특정 TIL 상세 조회
    @Query("SELECT * FROM til_items WHERE id = :id")
    suspend fun getTilById(id: Long): TilEntity?

    // 특정 월의 TIL들만 가져오기 (통계 및 회고 생성용)
    // timestamp를 활용해 해당 월의 시작~끝 범위 조회
    @Query("SELECT * FROM til_items WHERE createdAt BETWEEN :startTime AND :endTime ORDER BY createdAt ASC")
    suspend fun getTilsByMonth(
        startTime: Long,
        endTime: Long,
    ): List<TilEntity>

    // TIL 삭제 (스와이프로 삭제 시 사용)
    @Query("DELETE FROM til_items WHERE id = :id")
    suspend fun deleteTilById(id: Long)
}
