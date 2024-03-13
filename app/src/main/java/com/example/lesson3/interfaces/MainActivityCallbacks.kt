package com.example.lesson3.interfaces

import com.example.lesson3.NamesOfFragment
import com.example.lesson3.data.Invoice

interface MainActivityCallbacks {
    fun newTitle(_title: String)
    fun showFragment(fragmentType: NamesOfFragment, invoice: Invoice?= null)
}