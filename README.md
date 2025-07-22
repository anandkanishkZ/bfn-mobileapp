# 🩸 Blood for Nepal - Mobile App

<div align="center">

![Blood for Nepal Logo](logo-transparent.png)

**Connecting Blood Donors with Those in Need Across Nepal**

[![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![MIT License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

*Saving Lives, One Donation at a Time* 💝

</div>

---

## 🌟 Overview

Blood for Nepal is a life-saving mobile application designed to bridge the gap between blood donors and recipients across Nepal. Built with modern Android technologies, this app creates a seamless platform for emergency blood requests, donor registration, and community-driven healthcare support.

### 🎯 **Mission**
To create a robust, reliable, and user-friendly platform that ensures no one in Nepal goes without life-saving blood when they need it most.

---

## ✨ Features

### 🔐 **Authentication & Profile Management**
- **Secure Login/Registration** with Firebase Authentication
- **Profile Customization** with photo upload via Cloudinary
- **User Verification** system for trusted donors
- **Password Reset** functionality

### 🩸 **Blood Donation System**
- **Donor Registration** with blood type verification
- **Active/Inactive Status** management
- **Donation History** tracking
- **Last Donation Date** monitoring

### 🚨 **Emergency Blood Requests**
- **Instant Blood Requests** with urgency levels
- **Real-time Notifications** via Firebase Messaging
- **Location-based Matching** for nearby donors
- **Blood Type Compatibility** checking

### 🔍 **Smart Search & Discovery**
- **Advanced Filtering** by blood type, location, and availability
- **Donor Directory** with contact information
- **Request Dashboard** for ongoing needs
- **Geographic Coverage** across all Nepal provinces

### 📱 **Modern UI/UX**
- **Material Design 3** implementation
- **Dark/Light Theme** support
- **Responsive Layout** for all screen sizes
- **Intuitive Navigation** with bottom tabs
- **Smooth Animations** and transitions

---

## 🛠️ Technology Stack

### **Frontend**
- ![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=flat&logo=kotlin&logoColor=white) **Kotlin** - Primary programming language
- ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=android&logoColor=white) **Jetpack Compose** - Modern UI toolkit
- ![Material Design](https://img.shields.io/badge/Material%20Design%203-757575?style=flat&logo=material-design&logoColor=white) **Material Design 3** - UI components

### **Backend & Services**
- ![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat&logo=firebase&logoColor=black) **Firebase Suite**:
  - 🔐 Authentication
  - 🗄️ Firestore Database
  - 📊 Analytics
  - 💥 Crashlytics
  - 📲 Cloud Messaging
- ![Cloudinary](https://img.shields.io/badge/Cloudinary-3448C5?style=flat&logo=cloudinary&logoColor=white) **Cloudinary** - Image management

### **Architecture & Libraries**
- 🏗️ **MVVM Architecture** with Repository Pattern
- 💉 **Dagger Hilt** - Dependency Injection
- 🔄 **Kotlin Coroutines** - Asynchronous programming
- 🧭 **Navigation Component** - App navigation
- 🌐 **Retrofit** - Network requests
- 🖼️ **Coil** - Image loading

---

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Arctic Fox or later
- Android SDK 27+
- Firebase Project Setup
- Cloudinary Account

### **Installation**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/anandkanishkZ/blood-for-nepal.git
   cd blood-for-nepal
   ```

2. **Firebase Configuration**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Add your Android app to the project
   - Download `google-services.json` and place it in `app/` directory
   - Enable Authentication, Firestore, and Cloud Messaging

3. **Cloudinary Setup**
   - Create account at [Cloudinary](https://cloudinary.com)
   - Update credentials in `app/src/main/java/com/natrajtechnology/bfn/utils/CloudinaryConfig.kt`
   - Create upload preset: `blood_for_nepal_preset` (unsigned mode)

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### **Configuration Files**

Update these files with your credentials:

- `app/google-services.json` - Firebase configuration
- `app/src/main/java/com/natrajtechnology/bfn/utils/CloudinaryConfig.kt` - Cloudinary settings

---

## 📂 Project Structure

```
app/
├── src/main/java/com/natrajtechnology/bfn/
│   ├── data/                           # Data layer
│   │   ├── model/                      # Data models
│   │   ├── repository/                 # Repository implementations
│   │   └── network/                    # Network services
│   ├── di/                             # Dependency Injection
│   ├── presentation/                   # UI layer
│   │   ├── screens/                    # Compose screens
│   │   │   ├── auth/                   # Authentication screens
│   │   │   ├── home/                   # Home dashboard
│   │   │   ├── donors/                 # Donor management
│   │   │   ├── requests/               # Blood requests
│   │   │   └── profile/                # User profile
│   │   └── viewmodel/                  # ViewModels
│   └── utils/                          # Utility classes
└── res/                                # Resources
    ├── drawable/                       # Images and icons
    ├── values/                         # Strings, colors, themes
    └── layout/                         # XML layouts (if any)
```

## 🌍 Contributing

We welcome contributions from the community! Here's how you can help:

### **Ways to Contribute**
- 🐛 Report bugs and issues
- 💡 Suggest new features
- 🔧 Submit pull requests
- 📖 Improve documentation
- 🌐 Add translations for local languages

### **Development Guidelines**
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### **Code Standards**
- Follow Kotlin coding conventions
- Use meaningful commit messages
- Add documentation for new features
- Include unit tests where applicable

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🏢 Developer Information

<div align="center">

### **Zwicky Technology Pvt. Ltd.**

**Lead Developer: Anand KanishkZ**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-anandkanishkZ-blue?style=flat&logo=linkedin)](https://linkedin.com/in/anandkanishkZ)
[![Twitter](https://img.shields.io/badge/Twitter-anandkanishkZ-1DA1F2?style=flat&logo=twitter)](https://twitter.com/anandkanishkZ)
[![Instagram](https://img.shields.io/badge/Instagram-anandkanishkZ-E4405F?style=flat&logo=instagram)](https://instagram.com/anandkanishkZ)
[![GitHub](https://img.shields.io/badge/GitHub-anandkanishkZ-181717?style=flat&logo=github)](https://github.com/anandkanishkZ)
[![Facebook](https://img.shields.io/badge/Facebook-anandkanishkZ-1877F2?style=flat&logo=facebook)](https://facebook.com/anandkanishkZ)
[![TikTok](https://img.shields.io/badge/TikTok-anandkanishkZ-000000?style=flat&logo=tiktok)](https://tiktok.com/@anandkanishkZ)

**📧 Contact:** [info@zwickytechnology.com](mailto:info@zwickytechnology.com)

**🌐 Company Website:** [zwickytechnology.com](https://zwickytechnology.com)

</div>

---

## 🚀 **App Features Deep Dive**

### **🔐 Authentication System**
- Secure email/password authentication
- Password reset functionality
- User profile creation and management
- Session management with auto-login

### **🩸 Donor Management**
- Comprehensive donor registration
- Blood type verification
- Location-based donor discovery
- Availability status management
- Donation history tracking

### **🚨 Emergency Requests**
- Instant blood request creation
- Priority levels (Critical, Urgent, Normal)
- Real-time notifications to nearby donors
- Request status tracking
- Contact information sharing

### **📊 Analytics & Insights**
- Donation statistics
- Request fulfillment rates
- User engagement metrics
- Geographic distribution analysis

---

## 🔧 **Configuration & Setup**

### **Firebase Configuration**
```json
{
  "project_info": {
    "project_id": "blood-for-nepal",
    "project_number": "YOUR_PROJECT_NUMBER",
    "storage_bucket": "blood-for-nepal.appspot.com"
  }
}
```

### **Cloudinary Configuration**
```kotlin
object CloudinaryConfig {
    const val CLOUD_NAME = "your_cloud_name"
    const val API_KEY = "your_api_key"
    const val API_SECRET = "your_api_secret"
    const val UPLOAD_PRESET = "blood_for_nepal_preset"
}
```

---

## 🎨 **Design System**

### **Color Palette**
- **Primary**: Blood Red (#DC143C)
- **Secondary**: Medical Blue (#4285F4)
- **Success**: Life Green (#22C55E)
- **Warning**: Alert Orange (#FF9500)
- **Error**: Critical Red (#FF3B30)

### **Typography**
- **Headers**: Roboto Bold
- **Body**: Roboto Regular
- **Captions**: Roboto Light

---

## 🙏 Acknowledgments

- **Nepal Red Cross Society** - For inspiration and guidance
- **Open Source Community** - For amazing libraries and tools
- **Beta Testers** - For valuable feedback and testing
- **Medical Professionals** - For domain expertise and requirements
- **Firebase Team** - For excellent backend services
- **Cloudinary** - For image management solutions

---

## 📞 Support

Need help or have questions?

- 📧 **Email Support:** support@zwickytechnology.com
- 💬 **Community Discussions:** [GitHub Discussions](https://github.com/anandkanishkZ/blood-for-nepal/discussions)
- 🐛 **Bug Reports:** [GitHub Issues](https://github.com/anandkanishkZ/blood-for-nepal/issues)
- 📖 **Documentation:** [Wiki](https://github.com/anandkanishkZ/blood-for-nepal/wiki)
- 📱 **WhatsApp Support:** +977-9863871421

---

## 📊 Project Stats

![GitHub repo size](https://img.shields.io/github/repo-size/anandkanishkZ/blood-for-nepal)
![GitHub last commit](https://img.shields.io/github/last-commit/anandkanishkZ/blood-for-nepal)
![GitHub issues](https://img.shields.io/github/issues/anandkanishkZ/blood-for-nepal)
![GitHub pull requests](https://img.shields.io/github/issues-pr/anandkanishkZ/blood-for-nepal)
![GitHub stars](https://img.shields.io/github/stars/anandkanishkZ/blood-for-nepal?style=social)
![GitHub forks](https://img.shields.io/github/forks/anandkanishkZ/blood-for-nepal?style=social)

---

## 🗺️ **Roadmap**

### **Phase 1 - Current** ✅
- Basic donor registration
- Blood request creation
- User authentication
- Profile management

### **Phase 2 - In Progress** 🚧
- Real-time notifications
- Advanced search filters
- Donation history
- Emergency alerts

### **Phase 3 - Planned** 📋
- Hospital integration
- Blood bank management
- AI-powered matching
- Multi-language support

### **Phase 4 - Future** 🔮
- IoT device integration
- Blockchain verification
- International expansion
- Advanced analytics

---

<div align="center">

**Made with ❤️ for Nepal**

*"Together, we can save lives and build a healthier Nepal"*

⭐ **Star this repository if you found it helpful!** ⭐

![Made with Love](https://img.shields.io/badge/Made%20with-❤️-red.svg)
![For Nepal](https://img.shields.io/badge/For-🇳🇵%20Nepal-blue.svg)

</div>

---

*Last Updated: January 2025 | Version 1.0.0*

### 5. Build and Run
1. Open the project in Android Studio
2. Sync Gradle files
3. Connect an Android device or start an emulator
4. Click **Run** button or use `Ctrl+R`



## 🔑 **Key Components**

### Data Layer
- **Models**: User, BloodRequest, DonationRecord
- **Repositories**: AuthRepository, BloodRepository
- **Firebase Integration**: Firestore, Auth, Storage

### Presentation Layer
- **ViewModels**: AuthViewModel, BloodRequestViewModel
- **Screens**: LoginScreen, HomeScreen, ProfileScreen
- **Navigation**: Compose Navigation with type-safe routes

### Dependency Injection
- **Hilt Modules**: FirebaseModule for Firebase services
- **Application Class**: BFNApplication with @HiltAndroidApp

## 🎨 **UI/UX Features**
- Material3 Design System
- Dark/Light theme support
- Responsive layouts
- Intuitive navigation
- Blood type color coding
- Urgency indicators
- Modern card-based design

## 🌍 **Localization**
- English (Primary)
- Nepali (Planned)
- Extensible for other languages

## 🔒 **Security Features**
- Firebase Authentication
- Firestore Security Rules
- Input validation
- Error handling
- Secure data transmission

## 📱 **Supported Devices**
- Android 8.1+ (API 27+)
- Phone and tablet layouts
- Portrait and landscape orientations

## 🚀 **Future Enhancements**
- Push notifications
- Location-based services
- QR code scanning
- Emergency alerts
- Admin dashboard
- Blood bank integration
- Appointment scheduling
- Social sharing

## 🤝 **Contributing**
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 **Support**
For technical support or questions, contact:
- Email: support@zwickytechnology.com
- Phone: +977-9825733821

## 📄 **License**
This project is licensed under the MIT License - see the LICENSE file for details.

---

**Blood For Nepal** - *Save Lives, Donate Blood* 🩸❤️
