package com.example.lesson3.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ListDAO {
        @Query("SELECT * FROM managers order by name")
        fun getManagers(): Flow<List<Manager>>

        @Insert(entity = Manager::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertManager(manager: Manager)

        @Update(entity = Manager::class)
        suspend fun updateManager(manager: Manager)

        @Insert(entity = Manager::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAllManagers(managerList: List<Manager>)

        @Delete(entity = Manager::class)
        suspend fun deleteManager(manager: Manager)

        @Query("DELETE FROM managers")
        suspend fun deleteAllManagers()

        @Query("SELECT * FROM clients")
        fun getlAllClients(): Flow<List<Client>>

        @Query("SELECT * FROM clients where manager_id=:managerID")
        fun getManagerClients(managerID : UUID): Flow<List<Client>>

        @Insert(entity = Client::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertClient(client: Client)

        @Delete(entity = Client::class)
        suspend fun deleteClient(client: Client)

        @Query("DELETE FROM clients")
        suspend fun deleteAllClients()

        @Query("SELECT * FROM invoices")
        fun getAllInvoices(): Flow<List<Invoice>>

        @Query("SELECT * FROM invoices where client_id=:clientID")
        fun getClientInvoices(clientID : UUID): Flow<List<Invoice>>

        @Insert(entity = Invoice::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertInvoice(invoice: Invoice)

        @Delete(entity = Invoice::class)
        suspend fun deleteInvoice(invoice: Invoice)

        @Query("DELETE FROM invoices")
        suspend fun deleteAllInvoices()
}
