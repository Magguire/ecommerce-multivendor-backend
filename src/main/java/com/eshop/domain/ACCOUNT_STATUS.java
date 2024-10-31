package com.eshop.domain;

public enum ACCOUNT_STATUS {

    PENDING_VERIFICATION, // Account is created but not yet verified.
    ACTIVE, // Account is active and in good standing
    DEACTIVATED, // Account is deactivated, user may have chosen to deactivate account
    SUSPENDED, // Account is temporarily suspended, possibly due to violation
    BANNED, // Account is permanently banned due to severe violations
    CLOSED, // Account is permanently closed, possibly at user's request
    DORMANT, // Account has not been used for at least 6 months
    INACTIVE // Account has not been used for more than a year.
}
