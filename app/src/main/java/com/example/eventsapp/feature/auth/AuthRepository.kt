package com.example.eventsapp.feature.auth

interface AuthRepository {

         suspend fun login(
            email: String,
            password: String
        ):Result<Unit>

    suspend fun signup(
        email:String,
        password:String
    ):Result<Unit>
}



// Result( conatiner objct thet holds success or exception for failure )
// is success or failure , fn return Unit ( nothing , its like void of other languages)