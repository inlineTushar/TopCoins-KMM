package com.tushar.shared

import com.tushar.coinlist.CoinListViewModel
import com.tushar.coinlist.di.coinListModule
import com.tushar.core.di.coroutineModule
import com.tushar.data.di.dataModule
import com.tushar.data.di.platformDataModule
import com.tushar.domain.di.domainModule
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier

/**
 * Initialize Koin for iOS.
 * This should be called once when the iOS app starts.
 */
fun startKoin() {
    startKoin {
        modules(
            platformDataModule,  // iOS-specific implementations
            dataModule,          // Data layer
            domainModule,        // Domain layer
            coroutineModule,     // Core module
            coinListModule       // Feature modules
        )
    }
}

@Suppress("unused")
object KotlinDependencies : KoinComponent {
    fun getCoinListViewModel() = getKoin().get<CoinListViewModel>()
}

@BetaInteropApi
fun Koin.get(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null, null)
}

@BetaInteropApi
fun Koin.get(
    objCClass: ObjCClass,
    qualifier: Qualifier?,
    parameter: Any,
): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier) { parametersOf(parameter) }
}