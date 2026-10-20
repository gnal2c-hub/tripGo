# Ruta Rentable — prototipo Android

## Objetivo
Burbuja flotante para estimar la rentabilidad de ofertas de Uber en moto. La app no acepta ni rechaza viajes.

## Compilar automáticamente en GitHub
1. Subí el contenido de esta carpeta al repositorio GitHub `rutarentable`, manteniendo la estructura de carpetas (incluida `.github/workflows/build-apk.yml`).
2. Abrí la pestaña **Actions** del repositorio.
3. Si aparece una solicitud para habilitar GitHub Actions, aceptala.
4. Abrí **Compilar APK Ruta Rentable** y elegí **Run workflow** si no empezó automáticamente.
5. Cuando el proceso termine con una marca verde, abrí la ejecución y descargá **RutaRentable-debug-apk** en la sección **Artifacts**. Descomprimí el ZIP descargado para encontrar `app-debug.apk`.

## Funciones incluidas
- Valores iniciales: combustible $2300/L y consumo 28 km/L.
- Permiso explícito para mostrar una burbuja flotante.
- Burbuja movible con ingreso manual de tarifa, kilómetros y otros costos.
- Servicio de accesibilidad optativo y limitado al paquete de Uber Driver para inspeccionar el texto que Android exponga. No pulsa botones ni acepta/rechaza viajes.

## Limitaciones importantes
- Esta es una base de prototipo, no una aplicación terminada ni probada en un teléfono real.
- El lector de ofertas es experimental: Uber puede no exponer la tarifa o las distancias como texto accesible. No se garantiza la lectura automática.
- La configuración actual no suma de forma fiable la distancia hasta recoger al pasajero y la distancia con pasajero; para eso hay que validar cómo aparece cada dato en la oferta y mejorar el lector.
- El APK generado por `assembleDebug` es de depuración, no una versión de publicación.
- Probá la app estacionado y verificá los datos antes de confiar en la recomendación. El cálculo de combustible inicial es $2300 ÷ 28 ≈ $82,14/km. La ganancia mínima está pendiente de definir.
