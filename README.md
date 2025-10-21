# 📱 Gestor Turnos Laundry

Una aplicación Android nativa para gestionar turnos de lavarropas y secarropas en edificios con múltiples torres.

## ✨ Características

- 🏠 **Home Screen** - Próximo turno y estado de máquinas en tiempo real
- 📅 **Calendario Semanal** - Reservar turnos con selección visual intuitiva
- 📋 **Mis Turnos** - Lista y gestión completa de reservas
- 🎨 **Material 3** - UI moderna con Jetpack Compose
- 🏗️ **Clean Architecture** - ViewModels, Repositorios, Casos de Uso
- 📊 **Estados en tiempo real** - Libre, Reservado, En Uso, Mantenimiento

## 🚀 Descargar APK

[![Build APK](https://github.com/TU_USUARIO/proyecto-android-laundry/actions/workflows/build-apk.yml/badge.svg)](https://github.com/TU_USUARIO/proyecto-android-laundry/actions/workflows/build-apk.yml)

1. Ve a la sección **Actions** de este repositorio
2. Haz clic en la ejecución más reciente
3. Descarga el **app-debug** desde artifacts

## 🛠️ Tecnologías

- **Lenguaje:** Kotlin 2.0
- **UI:** Jetpack Compose + Material 3
- **Arquitectura:** Clean Architecture + MVVM
- **Base de datos:** Room Database
- **Inyección de dependencias:** Hilt
- **Navegación:** Navigation Compose
- **Networking:** Retrofit + OkHttp

## 📱 Pantallas

### Home
- Tarjeta de próximo turno con temporizador
- Grid de máquinas con estados visuales
- Pull-to-refresh y manejo de errores

### Calendario
- Vista semanal con slots de 30 minutos
- Selección múltiple de horarios consecutivos
- Validación de duración (60-240 min)
- Selector de máquinas

### Mis Turnos
- Lista filtrable de reservas
- Estados: Próximos, Activos, Completados
- Acciones: Iniciar, Editar, Cancelar

## 🏗️ Arquitectura

```
├── app/                 # MainActivity y navegación
├── core/               # Utilidades y constantes
├── data/               # Repositorios y Room database
├── domain/             # Casos de uso y modelos
├── user-ui/           # Pantallas de usuario
├── admin-ui/          # Panel de administración
└── sensors-api/       # Cliente MQTT para ESP32
```

## 🔧 Compilar Localmente

```bash
git clone https://github.com/TU_USUARIO/proyecto-android-laundry.git
cd proyecto-android-laundry
./gradlew assembleDebug
```

APK generado en: `app/build/outputs/apk/debug/app-debug.apk`

## 📋 Requisitos

- **Android 7.0+** (API 24)
- **2GB RAM** mínimo
- **50MB** espacio libre

## 🎯 Próximas Funcionalidades

- [ ] Integración con API REST
- [ ] Notificaciones push FCM
- [ ] Sensores ESP32 via MQTT
- [ ] Panel de administración completo
- [ ] Modo offline con sincronización

## 📄 Licencia

MIT License - Ver archivo LICENSE para detalles.

---

**🎉 ¡Proyecto completamente funcional y listo para usar!**