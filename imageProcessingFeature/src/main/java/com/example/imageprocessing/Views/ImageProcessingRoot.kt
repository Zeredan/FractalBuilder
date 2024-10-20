package com.example.imageprocessing.Views

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cg_lab2.R
import com.example.imageprocessing.ViewModels.ImageProcessingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ImageProcessingRoot(scrollToMe: () -> Unit){
    val context = LocalContext.current
    val vm = viewModel<ImageProcessingViewModel>(
        factory = object : ViewModelProvider.Factory{
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return ImageProcessingViewModel(context, scrollToMe, (context as ComponentActivity).intent) as T
            }
        }
    )
    vm.context = context
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
        TextWithPicture(
            vm,
            "1) Исходное изображение",
            vm.startingImageBitmap
        )
        {
            var launcher: ActivityResultLauncher<Intent>? = null
            launcher = (context as ComponentActivity).activityResultRegistry.register(
                "key1",
                ActivityResultContracts.StartActivityForResult()
            )
            { result ->
                vm.emitUri(result.data?.data)
                launcher!!.unregister()
            }
            launcher.launch(
                Intent(Intent.ACTION_PICK).apply{
                    this.type = "image/*"
                }
            )
        }
        TextWithPicture(
            vm,
            "1) Оттенки серого",
            vm.grayImageBitmap
        )
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .height(60.dp),
            onClick = {
                vm.processGrayImageBitmap()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Green
            )
        )
        {
            Text("Вычислить")
        }
        TextWithPicture(
            vm,
            "2) Монохроматика",
            vm.monoChromaticImageBitmap
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Slider(
                modifier = Modifier
                    .weight(0.5f),
                value = vm.monochromaticTreshold.toFloat(),
                onValueChange = {
                    vm.monochromaticTreshold = it.roundToInt()
                    vm.isMonochromaticStateChanged = true
                },
                valueRange = 0f..255f
            )
            val enabled = vm.isMonochromaticCalculated && vm.isMonochromaticStateChanged
            OutlinedButton(
                modifier = Modifier
                    .weight(0.5f)
                    .height(60.dp),
                enabled = enabled,
                onClick = {
                    vm.processMonochromaticImageBitmap()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Yellow,
                    disabledContainerColor = if (vm.isMonochromaticCalculated && !vm.isMonochromaticStateChanged) Color.hsl(120f, 1f, 0.3f) else Color.hsl(60f, 1f, 0.3f)
                ),
                shape = RoundedCornerShape(5.dp)
            )
            {
                Text(if (enabled) "Вычислить" else if (vm.isMonochromaticCalculated) "Вычислено" else "Вычисляется", fontSize = 20.sp, color = if (enabled) Color.Magenta else Color.Yellow)
            }
        }
        TextWithPicture(
            vm,
            "3) Сглаживание 10",
            vm.smooth10ImageBitmap
        )
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .height(60.dp),
            onClick = {
                vm.processSmooth10ImageBitmap()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Green
            )
        )
        {
            Text("Вычислить")
        }
        Divider(thickness = 3.dp, color = Color.White, modifier = Modifier.padding(30.dp))
        TextWithPicture(
            vm,
            "4) Скелетизация Зонга-Суэна: Изначальное изображение",
            vm.skeletizationStartingBitmap
        )
        {
            var launcher: ActivityResultLauncher<Intent>? = null
            launcher = (context as ComponentActivity).activityResultRegistry.register(
                "key1",
                ActivityResultContracts.StartActivityForResult()
            )
            { result ->
                vm.emitSkeletizationUri(result.data?.data)
                launcher!!.unregister()
            }
            launcher!!.launch(
                Intent(Intent.ACTION_PICK).apply{
                    this.type = "image/*"
                }
            )
        }
        TextWithPicture(
            vm,
            "4) Скелетизация Зонга-Суэна: Монохромное изображение",
            vm.skeletizationMonochromaticBitmap
        )
        var nc = rememberNavController()
        nc.popBackStack("a", true)
        Row(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Slider(
                modifier = Modifier
                    .weight(0.5f),
                value = vm.skeletizationMonochromaticTreshold.toFloat(),
                onValueChange = {
                    vm.skeletizationMonochromaticTreshold = it.roundToInt()
                },
                valueRange = 0f..255f
            )
            OutlinedButton(
                modifier = Modifier
                    .weight(0.5f)
                    .height(60.dp),
                onClick = {
                    vm.processMonochromaticSkeletizationImageBitmap(vm.skeletizationMonochromaticTreshold)
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Yellow,
                ),
                shape = RoundedCornerShape(5.dp)
            )
            {
                Text("Вычислить")
            }
        }
        TextWithPicture(
            vm,
            "4) Скелетизация Зонга-Суэна: Результат",
            vm.skeletizationProcessedBitmap
        )
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .height(60.dp),
            onClick = {
                vm.processSkeletizationImageBitmap()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Green
            )
        )
        {
            Text("Вычислить")
        }
        Divider(thickness = 3.dp, color = Color.White, modifier = Modifier.padding(30.dp))
        TextWithPicture(
            vm,
            "J.1) на монетке",
            vm.heights1ImageBitmap
        )
        TextWithPicture(
            vm,
            "J.2) на монетке",
            vm.heights2ImageBitmap
        )
        TextWithPicture(
            vm,
            "J.3) на монетке",
            vm.heights3ImageBitmap
        )
        TextWithPicture(
            vm,
            "J.4) на монетке",
            vm.heights4ImageBitmap
        )
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .height(60.dp),
            onClick = {
                vm.processHeightsImageBitmap()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Green
            )
        )
        {
            Text("Вычислить")
        }
    }
}