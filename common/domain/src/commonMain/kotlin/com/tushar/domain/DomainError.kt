package com.tushar.domain

sealed class DomainError : Throwable() {
    object NoConnectivity : DomainError() {
        private fun readResolve(): Any = NoConnectivity
    }

    object NetworkTimeout : DomainError() {
        private fun readResolve(): Any = NetworkTimeout
    }

    object UnknownError : DomainError() {
        private fun readResolve(): Any = UnknownError
    }
}
