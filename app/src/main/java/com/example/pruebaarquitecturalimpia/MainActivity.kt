package com.example.pruebaarquitecturalimpia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pruebaarquitecturalimpia.data.TareaRepositoryInMemoryImpl
import com.example.pruebaarquitecturalimpia.domain.usecase.AgregarTareaUseCase
import com.example.pruebaarquitecturalimpia.domain.usecase.AlternarEstadoTareaUseCase
import com.example.pruebaarquitecturalimpia.domain.usecase.ObtenerTareasUseCase
import com.example.pruebaarquitecturalimpia.presentation.ui.TareasScreen
import com.example.pruebaarquitecturalimpia.presentation.viewmodel.TareasViewModel
import com.example.pruebaarquitecturalimpia.ui.theme.PruebaArquitecturaLimpiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 1. Instanciación del repositorio (Infraestructura)
        val repositorio = TareaRepositoryInMemoryImpl()

        // 2. Instanciación de Casos de Uso (Dominio)
        val obtenerTareasUseCase = ObtenerTareasUseCase(repositorio)
        val agregarTareaUseCase = AgregarTareaUseCase(repositorio)
        val alternarEstadoTareaUseCase = AlternarEstadoTareaUseCase(repositorio)

        // 3. Configuración del Factory para el ViewModel (Presentación)
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TareasViewModel(
                    obtenerTareasUseCase,
                    agregarTareaUseCase,
                    alternarEstadoTareaUseCase
                ) as T
            }
        }

        setContent {
            PruebaArquitecturaLimpiaTheme  {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel: TareasViewModel = viewModel(factory = factory)
                    val uiState by viewModel.uiState.collectAsState()

                    TareasScreen(
                        tareas = uiState,
                        onAgregarTarea = viewModel::onAgregarTarea,
                        onAlternarEstado = viewModel::onAlternarEstado,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
