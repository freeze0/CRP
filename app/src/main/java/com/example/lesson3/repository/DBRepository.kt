package com.example.lesson3.repository

import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DBRepository {
    fun getManager(): Flow<List<Manager>>
    suspend fun insertManager(manager: Manager)
    suspend fun updateManager(manager: Manager)
    suspend fun insertAllManagers(managerList: List<Manager>)
    suspend fun deleteManager(manager: Manager)
    suspend fun deleteAllManagers()

    fun getAllClients(): Flow<List<Client>>
    fun getManagerClients(restaurantId: UUID): Flow<List<Client>>
    suspend fun insertClient(client: Client)
    suspend fun deleteClient(client: Client)
    suspend fun deleteAllClients()

    fun getAllInvoices(): Flow<List<Invoice>>
    fun getClientInvoices(courseID: UUID): Flow<List<Invoice>>
    suspend fun insertInvoice(invoice: Invoice)
    suspend fun deleteInvoice(invoice: Invoice)
    suspend fun deleteAllInvoices()
}
