EventHub – College Event Management Platform

EventHub is a modern Android application designed to simplify college event management by connecting students and event organizers on a single platform.

The project consists of **Android applications** sharing the same Firebase backend:

 **EventHub** – Student Application
**EventHub Organizer** – Organizer Application

Students can discover, register for about college events, while organizers can create and manage events in real time.

---

currently working on this project not completed  , some features are ready 
 
# Features

##  Student App

- User Registration & Login
- Secure Firebase Authentication
- Browse Upcoming Events
- Filter Events
- Event Details Screen
- Real-time Event Updates
- Profile Management

---

## Organizer App

- Organizer Authentication
- Create Events
- Edit Existing Events
- Delete Events
- Archive Events
- Upload Event Posters
- View Registered Students
- Manage Events in Real Time

---

# Architecture

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
- Maintainable Code

---

# Tech Stack

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

## Libraries

- Navigation Component
- View Binding
- Glide / Coil
- Firebase SDK

---

# Screens

## Student Application

- Login
- Register
- Home
- Event Details
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

#Authentication

The project supports two user roles.

- Student
- Organizer

Role-based access ensures that only organizers can create, edit, or delete events.

---

# Firebase Services Used

- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Firebase Cloud Messaging

---

# Project Structure

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

#  Key Functionalities

✔ Real-time event synchronization

✔ Secure authentication

✔ Event Discovery

✔ Event poster upload

✔ Separate student & organizer applications

---

#  Future Improvements

- Role-based authorization
- QR Code based attendance
- Calendar Integration
- Event Analytics Dashboard
- AI Event Recommendation
- Dark Mode
- Admin Web Dashboard

---
