package com.example.eventsapp.feature.auth

import com.example.eventsapp.data.remote.AuthApi
import com.example.eventsapp.data.repository.source.local.dao.UserDao
import com.example.eventsapp.data.repository.source.local.entity.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authApi: AuthApi,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth
                .signInWithEmailAndPassword(email.trim(), password)
                .await()

            synchronizeApplicationUser()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun signup(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth
                .createUserWithEmailAndPassword(email.trim(), password)
                .await()

            synchronizeApplicationUser()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private suspend fun synchronizeApplicationUser() {
        val firebaseUser = firebaseAuth.currentUser
            ?: throw IllegalStateException(
                "Firebase user is missing after authentication"
            )

        // Refresh the ID token before making the authenticated API call.
        firebaseUser.getIdToken(true).await()

        try {
            // The OkHttp interceptor attaches the current Firebase ID token.
            val response = authApi.getCurrentUser()

            check(response.success) {
                "The backend could not resolve the application user"
            }

            val user = response.data.user

            // Persist the backend identity and role in Room.
            // The UI should observe Room, not the Retrofit response.
            userDao.upsertProfile(
                UserEntity(
                    userId = user.id,
                    email = user.email,
                    role = user.role,
                    fullName = user.fullName
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                403 -> throw Exception(
                    "Access forbidden (403). Your account is not authorized or not registered in the backend database.",
                    e
                )
                401 -> throw Exception(
                    "Unauthorized (401). Invalid or expired authentication token.",
                    e
                )
                404 -> throw Exception(
                    "Backend endpoint '/auth/me' not found (404). Check backend routes.",
                    e
                )
                else -> throw Exception(
                    "Backend server error (HTTP ${e.code()}).",
                    e
                )
            }
        } catch (e: IOException) {
            throw Exception(
                "Unable to connect to backend server. Please check if your local server is running.",
                e
            )
        }
    }
}
