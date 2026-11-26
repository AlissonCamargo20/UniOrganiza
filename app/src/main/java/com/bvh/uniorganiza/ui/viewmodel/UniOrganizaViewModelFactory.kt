package com.bvh.uniorganiza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bvh.uniorganiza.data.repository.UniOrganizaRepository

class UniOrganizaViewModelFactory(
    private val repository: UniOrganizaRepository,
    private val usuarioId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UniOrganizaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UniOrganizaViewModel(
                repository,
                usuarioId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}