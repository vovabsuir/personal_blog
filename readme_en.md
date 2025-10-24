<div align="center">

# 📝 Personal Blog

![Java](https://img.shields.io/badge/Java-17+-orange?logo=openjdk)
![Spring](https://img.shields.io/badge/Spring-Boot_3.x-green?logo=spring)
![React](https://img.shields.io/badge/React-18-blue?logo=react)
![License](https://img.shields.io/badge/license-MIT-blue)

<h3>A modern blog platform with admin panel for content management</h3>

</div>

## 🌐 Project Overview

Personal Blog is a full-stack application that provides a modern blog platform with comprehensive content management capabilities. The project follows a clean architecture approach with well-separated frontend and backend components.

### Key Features:
- **Full CRUD operations** for blog articles
- **JWT-based authentication** for admin panel
- **Email subscription system** with confirmation
- **Article tagging and filtering**
- **Markdown content rendering**
- **Responsive UI** with modern design
- **Email notifications** for subscribers when new articles are published
- **Asynchronous email processing** to avoid blocking requests

## 🛠️ Technology Stack

### Backend
[![Java 17+](https://img.shields.io/badge/Java-17+-ED8B00?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring)](https://spring.io/projects/spring-boot)
[![JWT](https://img.shields.io/badge/JWT-521985?logo=jsonwebtokens)](https://jwt.io/)

### Frontend
[![React](https://img.shields.io/badge/React-18-blue?logo=react)](https://react.dev/)
[![React Router](https://img.shields.io/badge/React_Router-6.x-blue?logo=react)](https://reactrouter.com/)
[![Axios](https://img.shields.io/badge/Axios-1.x-5A29E4)](https://axios-http.com/)
[![Lucide](https://img.shields.io/badge/Lucide-038835)](https://lucide.dev/)

<details>
<summary>📦 Dependency list</summary>

**Backend:**
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Web
- Spring Mail
- Lombok
- JWT
- Java 17+

**Frontend:**
- React 18
- React Router
- Axios
- React Markdown
- Lucide React
</details>

## 📂 Project Structure

### Backend Structure

src/main/java/org/example/personalblog/     
├── config/ # Configuration classes   
├── controller/ # REST controllers  
├── dto/ # Data Transfer Objects    
├── entity/ # JPA entities  
├── exception/ # Custom exceptions  
├── mapper/ # Mappers for objects   
├── repository/ # Spring Data JPA repositories   
├── service/ # business logic    
└── util/ # Utilities and helper classes  

### Frontend Structure

src/    
├── components/ # Reusable components   
├── contexts/ # React contexts  
├── pages/ # App pages  
├── services/ # API services    
├── App.css # Global styles     
└── App.js # Main component 

## 🔐 Security Architecture

The application implements robust security measures:

- **JWT Authentication**: RSA-based tokens with configurable expiration
- **Role-based Authorization**: ADMIN role required for Create, Update and Delete operations
- **Password Encryption**: BCrypt for password storage
- **CORS Configuration**: Restricted to frontend origin
- **Input Validation**: DTO validation with Jakarta Validation
- **Error Handling**: Global exception handler for consistent error responses

Key security components:
- `JwtService`: JWT token generation and validation
- `JwtAuthFilter`: JWT authentication filter
- `SecurityConfig`: Spring Security configuration
- `SecurityManager`: Key management and password encoding

## 📡 API Endpoints

### Articles
- `GET /api/v1/articles/metadata` - Get article metadata
- `GET /api/v1/articles/metadata/tags` - Get articles by tags
- `GET /api/v1/articles/metadata/title` - Search articles by title
- `GET /api/v1/articles/content/{id}` - Get article content
- `GET /api/v1/articles/tags` - Get all distinct tags
- `POST /api/v1/articles` - Create new article (admin)
- `PUT /api/v1/articles/{id}` - Update article (admin)
- `DELETE /api/v1/articles/{id}` - Delete article (admin)

### Authentication
- `POST /api/v1/auth/login` - Admin login

### Subscriptions
- `POST /api/v1/subscriptions` - Subscribe to newsletter
- `GET /api/v1/subscriptions/confirm` - Confirm email subscription

## 🖥️ Frontend Architecture

The frontend follows React best practices with:
- **Context API** for authentication state management
- **Component-based architecture** with clear separation of concerns
- **API service layer** for clean data fetching
- **Modular components** for reusability
- **Responsive design** for all device sizes

Key components:
- **ArticleModal**: For creating/editing articles with file upload
- **ConfirmModal**: For confirmation dialogs (e.g., article deletion)
- **SubscriptionModal**: For email subscription with success state
- **AuthProvider**: Authentication context provider
- **AdminPage**: Full admin interface for content management
- **HomePage**: Main page with search, filtering, and pagination

## 📬 Email Subscription System

The application features a complete email subscription workflow:
1. User submits email via frontend
2. Backend generates JWT confirmation token
3. Confirmation email sent with link
4. User clicks link to confirm subscription
5. Confirmed subscribers receive notifications about new articles

Key components:
- `SubscriptionService`: Manages subscription logic with async email sending
- `EmailService`: Handles email sending with error logging
- Frontend subscription modal with success/error states

## 🧩 Multipart API Design

The backend uses a clever multipart approach for article creation and updates:

```java
@PostMapping
public ResponseEntity<ArticleMetadataResponse> addArticle(
    @RequestPart("metadata") ArticleRequest articleRequest,
    @RequestPart("content") MultipartFile content) {
    // ...
}
```

This allows sending:

* JSON metadata (title, slug, tags) as a JSON blob
* Markdown content as a separate file part
* All in a single HTTP request with multipart/form-data

## 🚀 Getting Started for Developers

### Prerequisites

* Java 17+
* Node.js 18+
* PostgreSQL (or compatible database)

### Backend Setup

1. Clone the repository
2. Set up environment variables:
```
ADMIN_LOGIN=your_login
ADMIN_PASSWORD=your_encrypted_password_using_bcrypt
FRONTEND_URI=http://localhost:3000
GMAIL_ADDRESS=your_email@gmail.com
PUBLIC_KEY=your_public_key
PRIVATE_KEY=your_private_key
ACCESS_EXPIRATION=3600
CONFIRMATION_URL=http://localhost:3000/confirm?
```
3. Run `mvn spring-boot:run` to start the backend

### Frontend Setup
1. Navigate to frontend directory
2. Run `npm install` to install dependencies
3. Run `npm run dev` to start the development server

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'feat: add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Create a new Pull Request

Areas where contributions are especially needed:

* **UI/UX** improvements
* Additional **features** like comments system
* Enhanced **search** functionality
* **Performance** optimizations
* Documentation improvements

---

<div align="center">

A modern, developer-friendly blog platform ready for customization and extension

</div>