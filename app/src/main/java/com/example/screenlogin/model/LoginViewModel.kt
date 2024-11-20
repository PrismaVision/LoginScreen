
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenlogin.RetrofitConfig
import com.example.screenlogin.repository.LoginRequest
import com.example.screenlogin.repository.LoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() { //ViewModel() segue o padrão de arquitetura MVVM

    private val loginStatus = MutableLiveData<LoginStatus>(LoginStatus.Loading) //Estado mutável
    val loginState: LiveData<LoginStatus> = loginStatus //Permite que o usuário possa ver as alterações


    fun login(email: String, password: String) {

         // Verifique se os campos não estão vazios
        if (email.isBlank() || password.isBlank()) {
            loginStatus.value = LoginStatus.Error("Email e Senha são obrigatórios.")
            return
        }

        val loginRequest = LoginRequest(email, password)
        // Faz a requisição para o servidor
        RetrofitConfig.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {

            private fun parseError(response: Response<*>): String { //O objeto Response<*> recebe uma resposta genérica da API e retorna uma String contendo a mensagem de erro
                Log.d("LoginViewModel", "HTTP Status Code: ${response.code()}")
                val errorBody = response.errorBody()?.string() //errorBody tenta acessar a resposta de erro e converte para String se n for nulo
                return if (!errorBody.isNullOrEmpty()) { //Verifica se a resposta é vazio ou nulo
                    try {
                        val errorJson = Gson().fromJson(errorBody, ErrorResponse::class.java)//Utiliza o Gson para converter o corpo da resposta de erro (errorBody) em um objeto da classe ErrorResponse.
                        errorJson.message ?: "Erro desconhecido da API" //Se o campo message do objeto ErrorResponse estiver preenchido, retorna a mensagem. Caso contrário, retorna a string padrão "Erro desconhecido.".
                    } catch (e: Exception) {
                        "JSON inválido" //Caso ocorra uma falha no parsing (exemplo: o JSON está malformado), retorna a string padrão "Erro desconhecido.".
                    }
                } else {
                    "Erro API!" //Se o corpo do erro for nulo ou vazio, retorna "Erro desconhecido.".
                }
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) { //Verifica se a resposta da API possui status HTTP indicando sucesso (exemplo: 200 OK).
                    response.body()?.let { loginResponse -> //Tenta acessar o corpo da resposta com body(). Se não for nulo, utiliza a função let para processar o objeto loginResponse.
                        loginStatus.value = LoginStatus.Success(loginResponse.token) //Define o estado do login como Success e passa o token retornado pela API.
                    } ?: run {
                        loginStatus.value = LoginStatus.Error("Resposta inválida da API.") //Caso o corpo da resposta (body) seja nulo, executa o bloco run e define o estado do login como Error, com uma mensagem indicando erro de resposta da API.
                    }
                } else {
                    loginStatus.value = LoginStatus.Error(parseError(response)) //Se a resposta não for bem-sucedida (exemplo: 401 Unauthorized), chama o método parseError para obter a mensagem de erro detalhada.
                                                                                //Define o estado do login como Error com a mensagem retornada.
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Caso ocorra um erro de rede ou de conexão
                loginStatus.value = LoginStatus.Error("Erro de conexão: ${t.message}")
            }
        })
    }
}
// Representa os diferentes estados do login: Carregando, Sucesso e Erro
sealed class LoginStatus {
    object Loading : LoginStatus()
    data class Success(val token: String?) : LoginStatus()
    data class Error(val message: String) : LoginStatus()
}
// Classe auxiliar para capturar a mensagem de erro da resposta
data class ErrorResponse(
    val message: String?
)

