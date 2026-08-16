# 🎉 EventHub – College Event Management Platform

EventHub is a modern Android application designed to simplify college event management by connecting students and event organizers on a single platform.

The project consists of **two Android applications** sharing the same Firebase backend:

- 📱 **EventHub** – Student Application
- 🛠 **EventHub Organizer** – Organizer Application

Students can discover, register for, and receive notifications about college events, while organizers can create and manage events in real time.

---

currently working on this project not completed 

# ✨ Features

## 👨‍🎓 Student App

- User Registration & Login
- Secure Firebase Authentication
- Browse Upcoming Events
- Search Events
- Filter Events
- Event Details Screen
- Register for Events
- Save Favorite Events
- View Registered Events
- Real-time Event Updates
- Push Notifications
- Profile Management

---

## 🏢 Organizer App

- Organizer Authentication
- Create Events
- Edit Existing Events
- Delete Events
- Archive Events
- Upload Event Posters
- View Registered Students
- Manage Events in Real Time

---

# 🏗 Architecture

The application follows Google's recommended **MVVM Architecture**.

```
Presentation Layer
│
├── Activities / Fragments
├── ViewModel
│
Domain Layer
│
├── Repository
│
Data Layer
│
├── Firebase Firestore
├── Firebase Authentication
├── Firebase Storage
└── Firebase Cloud Messaging
```

This architecture provides

- Separation of Concerns
- Easy Testing
- Better Scalability
- Maintainable Code

---

# 🛠 Tech Stack

## Language

- Kotlin

## UI

- Jetpack Compose

## Architecture

- MVVM
- Repository Pattern

## Backend

- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Firebase Cloud Messaging

## Libraries

- Navigation Component
- View Binding
- Glide / Coil
- Firebase SDK

---

# 📱 Screens

## Student Application

- Login
- Register
- Home
- Event Details
- Search
- Registered Events
- Saved Events
- Profile

---

## Organizer Application

- Dashboard
- Create Event
- Edit Event
- Event Management
- Registered Students
- Profile

---

# 🔐 Authentication

The project supports two user roles.

- Student
- Organizer

Role-based access ensures that only organizers can create, edit, or delete events.

---

# ☁ Firebase Services Used

- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Firebase Cloud Messaging

---

# 📂 Project Structure

```
EventHub/

├── app/
├── ui/
├── viewmodel/
├── repository/
├── model/
├── utils/
├── firebase/
└── resources/
```

---

# 🚀 Key Functionalities

✔ Real-time event synchronization

✔ Secure authentication

✔ Event registration

✔ Event poster upload

✔ Search & Filtering

✔ Push notifications

✔ Role-based authorization

✔ Separate student & organizer applications

---

# 📸 Screenshots

| Student App | Organizer App |
|--------------|---------------|
| Add screenshots here | Add screenshots here |

---

# 🔮 Future Improvements

- QR Code based attendance
- Calendar Integration
- Event Analytics Dashboard
- AI Event Recommendation
- Dark Mode
- Offline Support using Room
- Admin Web Dashboard

---
