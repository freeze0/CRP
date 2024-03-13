package com.example.lesson3.repository

import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import com.example.lesson3.database.ListDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineDBRepository(val dao: ListDAO): DBRepository {
    override fun getManager(): Flow<List<Manager>> =dao.getManagers()
    override suspend fun insertManager(manager: Manager) = dao.insertManager(manager)
    override suspend fun updateManager(manager: Manager) =dao.updateManager(manager)
    override suspend fun insertAllManagers(managerList: List<Manager>) =dao.insertAllManagers(managerList)
    override suspend fun deleteManager(manager: Manager) =dao.deleteManager(manager)
    override suspend fun deleteAllManagers() =dao.deleteAllManagers()

    override fun getAllClients(): Flow<List<Client>> =dao.getlAllClients()
    override fun getManagerClients(restaurantId : UUID): Flow<List<Client>> =dao.getManagerClients(restaurantId)
    override suspend fun insertClient(client: Client) =dao.insertClient(client)
    override suspend fun deleteClient(client: Client) =dao.deleteClient(client)
    override suspend fun deleteAllClients() =dao.deleteAllClients()

    override fun getAllInvoices(): Flow<List<Invoice>> =dao.getAllInvoices()
    override fun getClientInvoices(courseID : UUID): Flow<List<Invoice>> =dao.getClientInvoices(courseID)
    override suspend fun insertInvoice(invoice: Invoice) =dao.insertInvoice(invoice)
    override suspend fun deleteInvoice(invoice: Invoice) =dao.deleteInvoice(invoice)
    override suspend fun deleteAllInvoices() =dao.deleteAllInvoices()
}