package com.example.screenlogin.model

import ErrorResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenlogin.RetrofitConfig
import com.example.screenlogin.repository.RegisterRequest
import com.example.screenlogin.repository.RegisterResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    // Estado que mantém o status da requisição (Loading, Success, Error)
    private val registerStatus = MutableLiveData<RegisterStatus>(RegisterStatus.Loading)
    val registerState: LiveData<RegisterStatus> = registerStatus

    fun register(nickName: String, email: String, password: String) {
        // Inicializa o status como 'Loading'
        registerStatus.value = RegisterStatus.Loading

        // Verificação básica de campos vazios
        if (nickName.isBlank() || email.isBlank() || password.isBlank()) {
            registerStatus.value = RegisterStatus.Error("Todos os campos são obrigatórios.")
            return
        }
        // Verificação de espaço
        if (nickName.contains(" ") || email.contains(" ") || password.contains(" ")) {
            registerStatus.value = RegisterStatus.Error("Nome de usuário, email e senha não podem conter espaços.")
            return
        }
        // Verificação do comprimento da senha
        if (password.length < 6 || password.length > 16) {
            registerStatus.value = RegisterStatus.Error("A senha deve ter entre 6 e 16 caracteres.")
            return
        }

        // Criação do objeto de requisição
        val registerRequest = RegisterRequest(nickName, email, password)

        // Envia a requisição de registro ao servidor
        RetrofitConfig.apiService.register(registerRequest)
            .enqueue(object : Callback<RegisterResponse> {

                // Método auxiliar para processar erros do servidor
                private fun parseError(response: Response<*>): String {
                    val errorBody = response.errorBody()?.string()
                    return if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorJson = Gson().fromJson(errorBody, ErrorResponse::class.java)
                            errorJson.message ?: "Erro desconhecido da API."
                        } catch (e: Exception) {
                            "Erro JSON"
                        }
                    } else {
                        "Credenciais Inválidas"
                    }
                }


            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                Log.d("RegisterViewModel", "Código HTTP: ${response.code()}")
                Log.d("RegisterViewModel", "Corpo da resposta: ${response.body()}")
                Log.d("RegisterViewModel", "Erro da resposta: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    response.body()?.let { registerResponse ->
                        registerStatus.value = RegisterStatus.Success(registerResponse.message)
                    } ?: run {
                        registerStatus.value = RegisterStatus.Error("Resposta inválida da API.")
                    }
                } else {
                        registerStatus.value = RegisterStatus.Error("Email existente!")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterViewModel", "Falha na requisição: ${t.message}")
                registerStatus.value = RegisterStatus.Error("Erro de conexão: ${t.message}")
            }
        })
    }


}

// Estados possíveis durante o registro
sealed class RegisterStatus {
    object Loading : RegisterStatus()
    data class Success(val message: String) : RegisterStatus()
    data class Error(val message: String) : RegisterStatus()
}
