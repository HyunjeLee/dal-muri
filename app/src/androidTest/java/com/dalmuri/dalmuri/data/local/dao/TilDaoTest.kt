package com.dalmuri.dalmuri.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dalmuri.dalmuri.data.local.AppDatabase
import com.dalmuri.dalmuri.data.local.entity.TilEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TilDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var tilDao: TilDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        tilDao = db.tilDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndReadTil() =
        runBlocking {
            val til =
                TilEntity(
                    title = "오늘의 학습",
                    learned = "Room Database testing",
                    createdAt = System.currentTimeMillis(),
                )
            val id = tilDao.insertTil(til)

            val loaded = tilDao.getTilById(id)
            assertNotNull(loaded)
            assertEquals(til.title, loaded?.title)
            assertEquals(til.learned, loaded?.learned)
        }

    @Test
    fun getAllTilsAsFlow() =
        runBlocking {
            val til1 = TilEntity(title = "Title 1", learned = "L1", createdAt = 1000L)
            val til2 = TilEntity(title = "Title 2", learned = "L2", createdAt = 2000L)

            tilDao.insertTil(til1)
            tilDao.insertTil(til2)

            val allTils = tilDao.getAllTils().first()

            assertEquals(2, allTils.size)
            // Check order (DESC by createdAt)
            assertEquals("Title 2", allTils[0].title)
            assertEquals("Title 1", allTils[1].title)
        }

    @Test
    fun getTilsByMonthRange() =
        runBlocking {
            // Feb 2025 range
            val startOfFeb = 1738368000000L // 2025-02-01 00:00:00
            val endOfFeb = 1740787199000L // 2025-02-28 23:59:59

            val febTil = TilEntity(title = "Feb", learned = "L", createdAt = 1738454400000L) // Feb 2nd
            val janTil = TilEntity(title = "Jan", learned = "L", createdAt = 1735689600000L) // Jan 1st

            tilDao.insertTil(febTil)
            tilDao.insertTil(janTil)

            val results = tilDao.getTilsByMonth(startOfFeb, endOfFeb)

            assertEquals(1, results.size)
            assertEquals("Feb", results[0].title)
        }
}
