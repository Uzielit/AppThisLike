package com.utez.thislike.ui.screens
import androidx.compose.foundation.BorderStroke
import com.utez.thislike.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utez.thislike.ViewModel.LoginViewModel
import com.utez.thislike.ui.components.ThisLikeInput
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.utez.thislike.ui.components.FotoCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import com.utez.thislike.ui.components.ThisLikeButton

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
){
    val viewModel: LoginViewModel = viewModel()

    val usuario by viewModel.usuario.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loginResult by viewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        if (loginResult?.isSuccess == true) {
            onLoginSuccess()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){



        LogoApp(imageRes = R.drawable.gato2)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Bienvenido", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))



        ThisLikeInput(
            value = usuario,
            onValueChange = viewModel::onUsuarioChange,
            label = "Usuario"
        )
        Spacer(modifier = Modifier.height(18.dp))

        ThisLikeInput(
            value = password,
            onValueChange = viewModel::onPasswordChange,
            label = "Contraseña",
            isPassword = true
        )

        if (viewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = viewModel.errorMessage!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            ThisLikeButton(
                text = "Iniciar Sesion",
                onClick = { viewModel.login() },
                modifier = Modifier.width(250.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate aquí", color = Color.Blue)
        }
    }

}

@Composable
fun LogoApp(imageRes: Int, size: Int = 150) {

    val appleShape = RoundedCornerShape(32.dp)

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Logo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(appleShape)
            .border(BorderStroke(2.dp, Color.LightGray), appleShape)
    )
}
