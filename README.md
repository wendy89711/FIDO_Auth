# FIDO Mobile authentication

- [繁體中文版](README_zh.md)
- A **FIDO Mobile Authentication App** combining **Android (Java) App** and **Web (ASP.NET MVC)**.  
  In collaboration with Cathay General Hospital, the purpose is to **bind the mobile device IMEI for identity verification**, so patients can receive medical services without carrying their National Health Insurance (NHI) card.  
  Functions include: *App scans webpage QRCode* / *Web inputs App verification code* to bind the mobile IMEI.


![Project Demo](demo_image/UI_US5NET.png)

## System Requirements

- **Android App**
    - **Development Tool**: Android Studio 4.1.1 or higher (latest version recommended)
    - **JDK**: 1.8 (Java 8 compatible)
    - **Android SDK**:
        - Compile SDK Version: 29
        - Target SDK Version: 29
        - Min SDK Version: 19
    - **Device Requirements**:
        - Android smartphones/tablets, Android 4.4 (KitKat, API 19) or above

- **Web**
    - **IDE**: Visual Studio 2019 or 2022 (with .NET development workload)
    - **Database**: MySQL



## Project Structure
```bash
## Project Structure
```bash
FIDO_Auth/                  
├─ app/src/main/java/com/fju/seminar
│  ├─ Biometrics.java           # Login with biometric authentication
│  ├─ DeviceBind.java           # Device binding
│  ├─ LoginActivity.java        # Login
│  ├─ MainActivity.java         # Home
│  ├─ QRLogin.java              # QRCode binding
│  ├─ WebLogin.java             # Verification code binding
│  └─ WelcomeActivity.java      # App launch interface
├─ Web
│   ├─ Controllers
│   │  ├─ RegisterController.cs # Controls registration interface
│   │  └─ HomeController.cs     # Controls homepage
│   ├─ Models                   # Auto-generated files
│   ├─ Views
│   │  ├─ Home                  # Pages: Home, About, Contact
│   │  ├─ Register              # Registration-related pages, e.g., CRUD for devices
│   │  └─ Shared                # Shared views, e.g., error page
│   └─ ...
└─ ...                    

```
