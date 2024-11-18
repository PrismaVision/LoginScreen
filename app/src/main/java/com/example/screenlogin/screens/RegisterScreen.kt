package com.example.screenlogin.screens

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CheckboxDefaults.colors
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
import androidx.navigation.NavController
import com.example.screenlogin.R
import com.example.screenlogin.model.RegisterViewModel
import com.example.screenlogin.repository.RegisterRequest
import com.example.screenlogin.model.RegisterViewModel.RegisterStatus

@Composable
fun singUpScreen(navController: NavController, viewModelRegister: RegisterViewModel) {
    val registerState by viewModelRegister.registerState.observeAsState(RegisterViewModel.RegisterStatus.Loading)
    val context = LocalContext.current

    var nickName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )  {

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
            text = "Register to access your account",
            fontSize = 16.sp,
            color = Color.Gray
        )


        Spacer(modifier = Modifier.height(130.dp))



        //Campo de Nickname


        OutlinedTextField(value = nickName,
            onValueChange = {nickName = it},
            label = { Text(text = "Nickname") },
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "User Icon")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        //Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(text = "Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Email, contentDescription = "User Icon")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        //Campo senha
        var passwordVisible by remember { mutableStateOf(false) } //Variavel que controla a visibilidade da senha
        OutlinedTextField(value = password,
            onValueChange = {password = it},
            label = { Text(text = "Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Lock, contentDescription = "Password Icon")
            },
            //Ativa o modo de  visualização da senha
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff //Icone de visibilidade
                val description = if (passwordVisible) "Hide password" else "Show password" //Descrição do icone
                IconButton(onClick = { passwordVisible = !passwordVisible }) { //Mudança de estado do icone
                    Icon(imageVector = icon, contentDescription = description) //Icone
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), shape = RoundedCornerShape(14.dp)

        )

        //CheckBox Dos Termos de Condições

        var checked by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
        ) {
            // Checkbox para aceitar os termos
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = colors(checkmarkColor = Color.White)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Texto com link para os termos de serviço
            Text(
                text = "I have read and ",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = "agree to the terms of service",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = Modifier.clickable { showDialog = true }.align(Alignment.CenterVertically)
            )
        }

        // Dialogo para exibir os Termos e Condições
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Terms and Conditions") },
                text = { Text("Aqui você coloca o texto dos Termos e Condições...") },
                confirmButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Agree")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        //Botao Registrar
        Button(
            onClick = {viewModelRegister.register(nickName, email, password) },
            enabled = checked,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(14.dp)

        ) {
            Text(text = "Register",color = Color.White)

        }
        Spacer(modifier = Modifier.height(70.dp))



        /* esta funçao tem estar dentro do botao
        Spacer(modifier = Modifier.height(8.dp))

        if (registerState is RegisterViewModel.RegisterStatus.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        */
        LaunchedEffect(registerState) {
            when (registerState) {
                is RegisterStatus.Success -> {
                    // Exibe Toast com a mensagem de sucesso
                    Toast.makeText(context, (registerState as RegisterStatus.Success).message, Toast.LENGTH_SHORT).show()

                    // Limpa os campos
                    nickName = ""
                    email = ""
                    password = ""

                    // Navega para a tela de login
                    navController.navigate("Login") {
                        popUpTo("Register") { inclusive = true }
                    }
                }
                is RegisterStatus.Error -> {
                    // Exibe Toast com a mensagem de erro
                    Toast.makeText(context, (registerState as RegisterStatus.Error).message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}
    }
//Cria uma função nova em que possa ver o preview
@Preview(showBackground = true)
@Composable
fun SingUpScreenPreview() {
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

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )  {

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
                text = "Register to access your account",
                fontSize = 16.sp,
                color = Color.Gray
            )


            Spacer(modifier = Modifier.height(130.dp))

        //Campo de Nickname


            OutlinedTextField(value = "VARIAVEL NICKNAME",
                onValueChange = {},
                label = { Text(text = "Nickname") },
                singleLine = true,
                leadingIcon = {
                        Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "User Icon")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )



            Spacer(modifier = Modifier.height(40.dp))

        //Campo de Email
            OutlinedTextField(value = "VARIAVEL EMAIL",
                onValueChange = {},
                label = { Text(text = "Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "User Icon")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))//Da espaço entre os campos
    //Campo senha
            var passwordVisible by remember { mutableStateOf(false) } //Variavel que controla a visibilidade da senha
            OutlinedTextField(value = "vARIAVEL password",
                onValueChange = { " password = it " },
                label = { Text(text = "Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "Password Icon")
                },
                //Ativa o modo de  visualização da senha
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff //Icone de visibilidade
                    val description = if (passwordVisible) "Hide password" else "Show password" //Descrição do icone
                    IconButton(onClick = { passwordVisible = !passwordVisible }) { //Mudança de estado do icone
                        Icon(imageVector = icon, contentDescription = description) //Icone
                    }
                },
                modifier = Modifier.fillMaxWidth(),shape = RoundedCornerShape(14.dp)

            )

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = { " viewModelRegister.register(nickName, email, password) " },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.width(200.dp),
                shape = RoundedCornerShape(14.dp)

            ) {
                Text(text = "Register",color = Color.White)

            }






        }
    }
}