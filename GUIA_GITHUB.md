# 🚀 Guía Completa: GitHub → APK Automático

## 📋 **PASO 1: Crear cuenta en GitHub (si no tienes)**

1. Ve a **https://github.com**
2. Haz clic en **"Sign up"**
3. Completa:
   - Username (ej: `zupel-dev`)
   - Email
   - Password
4. Verifica tu email
5. **¡Listo!** Ya tienes cuenta GitHub

---

## 📁 **PASO 2: Crear nuevo repositorio**

1. **Inicia sesión** en GitHub
2. Haz clic en **"+"** (esquina superior derecha)
3. Selecciona **"New repository"**
4. Completa:
   - **Repository name:** `gestor-turnos-laundry`
   - **Description:** `App Android para gestión de turnos de laundromat`
   - ✅ **Public** (para que GitHub Actions sea gratis)
   - ✅ **Add a README file**
5. Haz clic en **"Create repository"**

---

## 📤 **PASO 3: Subir tu proyecto**

### **Opción A: Desde la web (Más fácil)**

1. En tu nuevo repositorio, haz clic en **"uploading an existing file"**
2. **Arrastra y suelta** toda la carpeta `"proyecto android laundry"`
3. O haz clic en **"choose your files"** y selecciona todos los archivos
4. Scroll down y escribe:
   - **Commit message:** `Initial commit - Complete Android project`
5. Haz clic en **"Commit changes"**

### **Opción B: Desde terminal (Si tienes Git)**

```bash
# En la carpeta del proyecto:
cd "proyecto android laundry"
git init
git add .
git commit -m "Initial commit - Complete Android project"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/gestor-turnos-laundry.git
git push -u origin main
```

---

## ⚙️ **PASO 4: Activar GitHub Actions**

1. En tu repositorio, haz clic en la pestaña **"Actions"**
2. GitHub detectará automáticamente el archivo `.github/workflows/build-apk.yml`
3. Haz clic en **"I understand my workflows, go ahead and enable them"**
4. **¡Listo!** GitHub Actions está activado

---

## 🏗️ **PASO 5: Compilar automáticamente**

### **Opción 1: Automático (al subir archivos)**
- GitHub compilará automáticamente cada vez que subas cambios

### **Opción 2: Manual**
1. Ve a **Actions** tab
2. Haz clic en **"Build APK"** (workflow)
3. Haz clic en **"Run workflow"** (botón azul)
4. Haz clic en **"Run workflow"** para confirmar

---

## 📱 **PASO 6: Descargar tu APK**

1. Ve a **Actions** tab
2. Haz clic en la ejecución más reciente (circulo verde ✅)
3. Scroll down hasta **"Artifacts"**
4. Haz clic en **"app-debug"** para descargar
5. **¡Descomprime el ZIP!** → Dentro está tu `app-debug.apk`

---

## 📲 **PASO 7: Instalar en tu celular**

### **Método 1: Cable USB**
1. Conecta tu celular a la PC
2. Copia `app-debug.apk` al celular
3. En el celular: Configuración > Seguridad > Permitir instalación de apps desconocidas
4. Abre el archivo APK desde el explorador de archivos
5. **¡Instalar!**

### **Método 2: ADB (Si tienes)**
```bash
adb install app-debug.apk
```

### **Método 3: Google Drive/Email**
1. Sube el APK a Google Drive
2. Descárgalo desde tu celular
3. Instálalo

---

## 🎯 **RESUMEN VISUAL:**

```
📁 Tu PC → 📤 GitHub → ⚙️ Actions → 🏗️ Compilar → 📱 APK
   └─ Upload     └─ Automático    └─ Download
```

## ⏱️ **Tiempos aproximados:**
- **Subir proyecto:** 5-10 minutos
- **Compilación:** 5-8 minutos
- **Descarga APK:** 1 minuto
- **Total:** ~15 minutos

## 🆘 **Si algo sale mal:**

### **Error en compilation:**
- Ve a Actions > Clic en el workflow fallido
- Revisa los logs para ver qué pasó
- La mayoría de errores son por archivos faltantes

### **No aparece el APK:**
- Espera que termine la compilación (círculo verde)
- El APK está en "Artifacts", no en "Files"

### **No puedo instalar:**
- Habilita "Fuentes desconocidas" en Android
- Verifica que sea un `.apk` válido

---

**🎉 ¡En 15 minutos tendrás tu APK funcionando!**

¿Comenzamos? ¿En qué paso necesitas ayuda específica?