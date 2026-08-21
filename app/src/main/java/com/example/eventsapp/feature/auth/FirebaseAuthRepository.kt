package com.example.eventsapp.feature.auth
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun login(email: String  ,password: String): Result<Unit>{
        return try{
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Result.success(Unit)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signup(email:String, password: String ): Result<Unit>{
      return  try{
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            Result.success(Unit)
        }
        catch(e: Exception){
            Result.failure(e)
        }
    }
}