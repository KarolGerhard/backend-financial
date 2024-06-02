package com.gerhard.financial.transaction

import com.gerhard.financial.security.UserToken

data class TransactionFilter(
    val type: TransactionType?,
    val sortDir: SortDir?,
    val userToken: UserToken?
)