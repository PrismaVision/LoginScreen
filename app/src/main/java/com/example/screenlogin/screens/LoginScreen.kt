package com.example.screenlogin.screens

import LoginStatus
import LoginViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Visibility
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.screenlogin.R

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val loginState by viewModel.loginState.observeAsState(LoginStatus.Loading)
    val context = LocalContext.current


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //Mostra o fundo da tela  borrado
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de fundo desfocada
        Image(
            painter = painterResource(id = R.drawable.background_image), // Substitua pelo seu recurso de imagem
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp), // Desfoca a imagem  de fundo
            contentScale = ContentScale.Crop
        )

        //Conteudo Principal
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            //Imagem da logo
            Image(
                painter = painterResource(id = R.drawable.a),
                contentDescription = "ImageLogin",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Prisma Vision",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Login to access your account",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(130.dp))//Da espaço entre os campos

            //Campo email


            OutlinedTextField(
                value = email,
                onValueChange = {email = it}, /* Atualiza o valor do Email */
                label = { Text(text = "Email", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp) // Deixa a caixa mais arredondada
            )


            Spacer(modifier = Modifier.height(30.dp))//Da espaço entre os campos

            // Campo de senha


            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},/* Atualiza o valor da senha */
                label = { Text(text = "Password", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                },
                //Ativa o modo de  visualização da senha
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp) // Deixa a caixa mais arredondada
            )
            Text(
                text = "Forgot Password?",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp, end = 16.dp)
                    .clickable {
                        // Ação ao clicar no texto, como navegação para a tela de recuperação de senha
                    }
            )
            Spacer(modifier = Modifier.height(70.dp))//Da espaço entre os campos

            // Botão de Login
            Button(

                onClick = {viewModel.login(email, password)},  /* Ação de login */
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.width(200.dp),
                shape = RoundedCornerShape(14.dp) // Deixa a caixa mais arredondada
            ) {
                Text(text = "Login", color = Color.White)
            }
          



            Spacer(modifier = Modifier.height(140.dp))

            // Texto para Sign Up
            Row {
                TextButton(onClick ={
                    navController.navigate("SingUp")

                    }){
                        Text(text = "Don’t have an account?", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Sign Up", color = Color.Blue)
                        //verificar para botar um nav
                    }
                }
            }

            /*
           * Nao esta funcionando *

        Exibir o carregamento se o estado for Loading
        if (loginState is LoginStatus.Loading) {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
       }
        */

            // Exibir Toast para Success ou Error
            LaunchedEffect(loginState) {
                when (loginState) {
                    is LoginStatus.Success -> {
                        Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    }

                    is LoginStatus.Error -> {
                        val errorMessage = (loginState as LoginStatus.Error).message
                        Toast.makeText(context, "$errorMessage", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

//Cria uma função nova em que possa ver o preview
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    //mostrar o fundo borrado
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de fundo desfocada
        Image(
            painter = painterResource(id = R.drawable.background_image), // Substitua pelo seu recurso de imagem
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp), // Desfoca a imagem com 20.dp
            contentScale = ContentScale.Crop
        )

        //Conteudo Principal
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = "ImageLogin",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Prisma Vision",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Login to access your account",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(130.dp))//Da espaço entre os campos e este da o espaço do conjunto da logo para o conjunto das labels

        // Campo de e-mail
        Text(
            text = "E-mail Address",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 4.dp) // Ajusta o posicionamento do texto
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Enter your Email")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp) // Deixa a caixa mais arredondada
        )

        Spacer(modifier = Modifier.height(30.dp))//Da espaço entre os campos

        // Campo de senha
        Text(
            text = "Password",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 4.dp) // Ajusta o posicionamento do texto
        )

        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = "",
            onValueChange = { /* Atualizar valor da senha */ },
            label = { Text(text = "Enter your Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            //Ativa o modo de  visualização da senha
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp) // Deixa a caixa mais arredondada
        )

        Text(
            text = "Forgot Password?",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp, end = 16.dp)
                .clickable {
                    // Ação ao clicar no texto, como navegação para a tela de recuperação de senha
                }
        )
        Spacer(modifier = Modifier.height(70.dp))//Da espaço entre os campos

        // Botão de Login
        Button(
            onClick = { /* Ação de login */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(14.dp) // Deixa a caixa mais arredondada
        ) {
            Text(text = "Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Or Sign In With",
            color = Color.Gray
        )




        Spacer(modifier = Modifier.height(60.dp))

        // Texto para Sign Up
        Row {
            Text(text = "Don’t have an account?")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign Up", color = Color.Blue)
    //verificar para botar um nav

        }
    }
}
}

