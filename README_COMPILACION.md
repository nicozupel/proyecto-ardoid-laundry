# Gestor Turnos Laundry - Guía de Compilación

## 📱 Proyecto Completado

Este proyecto de **Gestor Turnos Laundry** está completamente implementado con:

- ✅ **Pantalla Home** - Próximo turno y estado de máquinas
- ✅ **Calendario Semanal** - Reservar turnos con selección visual
- ✅ **Mis Turnos** - Lista y gestión de reservas
- ✅ **Arquitectura Clean** - ViewModels, Repositorios, Casos de Uso
- ✅ **Material 3** - UI moderna con Jetpack Compose
- ✅ **Datos de Prueba** - Para visualizar la funcionalidad

## 🛠️ Requisitos para Compilar

### 1. **Android Studio**
- Android Studio Iguana o superior
- SDK mínimo: API 24 (Android 7.0)
- SDK objetivo: API 34 (Android 14)

### 2. **Java/Kotlin**
- JDK 17 o superior
- Kotlin 2.0.0

## 🚀 Pasos para Compilar

### 1. **Abrir en Android Studio**
```bash
# Abre Android Studio y selecciona:
# File > Open > Selecciona la carpeta "proyecto android laundry"
```

### 2. **Sync del Proyecto**
- Android Studio detectará automáticamente el proyecto Gradle
- Haz clic en "Sync Now" cuando aparezca la notificación
- Espera a que descargue todas las dependencias

### 3. **Compilar APK**
```bash
# Desde terminal en la carpeta del proyecto:
./gradlew assembleDebug

# O desde Android Studio:
# Build > Build Bundle(s) / APK(s) > Build APK(s)
```

### 4. **Instalar en Dispositivo**
```bash
# Con dispositivo Android conectado:
./gradlew installDebug

# O desde Android Studio:
# Run > Run 'app'
```

## 📁 Estructura del Proyecto

```
proyecto android laundry/
├── app/                    # Módulo principal Android
├── core/                   # Utilidades y constantes
├── data/                   # Repositorios y Room database
├── domain/                 # Casos de uso y modelos
├── user-ui/               # Pantallas de usuario
├── admin-ui/              # Pantallas de administrador
└── sensors-api/           # Cliente MQTT para sensores
```

## 🎯 Funcionalidades Implementadas

### **Pantalla Home**
- Tarjeta de próximo turno con temporizador
- Grid de máquinas con estados visuales
- Navegación al calendario para reservar

### **Calendario**
- Vista semanal con slots de 30 minutos
- Selección múltiple de horarios consecutivos
- Selector de máquinas
- Validación de duración (60-240 min)
- Confirmación de reserva

### **Mis Turnos**
- Lista de todas las reservas
- Filtros: Todos, Próximos, Activos, Completados
- Acciones: Iniciar, Editar, Cancelar
- Estados visuales diferenciados

## 🧪 Datos de Prueba

La app incluye datos de prueba para demostración:
- **5 máquinas** con diferentes estados
- **5 reservas** en diferentes estados
- **Estados**: Libre, En Uso, Reservado, Mantenimiento

## 🔧 Configuración Adicional

### **Base de Datos**
- Configurada con Room pero usando datos mock
- Estructura completa para 10+ entidades
- Migraciones preparadas

### **Notificaciones**
- Firebase configurado en build.gradle
- Agregar google-services.json cuando tengas Firebase

### **API Backend**
- Repositorios preparados para integrar API REST
- Cambiar URLs en NetworkModule.kt

## 📱 APK de Salida

El APK compilado estará en:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🐛 Solución de Problemas

### **Error de Sincronización**
- Verifica conexión a internet
- File > Invalidate Caches > Restart

### **Error de Compilación**
- Verifica JDK 17+ instalado
- Build > Clean Project > Rebuild Project

### **Erro de Dependencias**
- Revisa libs.versions.toml
- Sync Project with Gradle Files

## 📞 Soporte

El proyecto está completo y listo para compilar. Todas las dependencias están configuradas y las pantallas principales implementadas con datos de prueba funcionales.

**¡La app está lista para ejecutar y probar! 🎉**