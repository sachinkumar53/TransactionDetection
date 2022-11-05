package com.sachin.app.smsdetection

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.sachin.app.smsdetection.screen.NavGraphs
import com.sachin.app.smsdetection.ui.theme.SMSDetectionTheme
import dev.marcelpibi.permissionktx.compose.rememberLauncherForPermissionResult
import dev.marcelpinto.permissionktx.Permission

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SMSDetectionTheme {
                // A surface container using the 'background' color from the theme
                val permission = rememberLauncherForPermissionResult(
                    Permission(
                        Manifest.permission.READ_SMS
                    )
                ) { isGranted ->
                    if (isGranted) viewModel.loadSMS()
                }


                LaunchedEffect(key1 = permission.type.status) {
                    if (!permission.type.status.isGranted()) {
                        permission.launch(Unit)
                    } else viewModel.loadSMS()
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(viewModel)
                        }
                    )
                }
            }
        }
    }

}
