# Personal Blog Frontend

A modern, minimalist React frontend for the Personal Blog Spring Boot backend.

## Features

- **Modern Design**: Clean, minimalist interface with card-based article display
- **Responsive Layout**: Works seamlessly on desktop and mobile devices
- **Markdown Support**: Full markdown rendering for article content
- **Admin Panel**: Complete CRUD operations for article management
- **Authentication**: JWT-based admin authentication
- **Pagination**: Navigate through articles with pagination controls

## Tech Stack

- **React 18** - Modern React with hooks
- **Vite** - Fast build tool and dev server
- **React Router** - Client-side routing
- **React Markdown** - Markdown rendering
- **Axios** - HTTP client for API calls
- **Lucide React** - Modern icon library

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Running Spring Boot backend on `http://localhost:8080`

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd Frontend/personal-blog-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Open your browser and visit `http://localhost:5173`

## Usage

### For Visitors

1. **Browse Articles**: Visit the homepage to see all published articles in a card layout
2. **Read Articles**: Click on any article card to read the full content
3. **Navigate**: Use pagination controls to browse through multiple pages of articles

### For Administrators

1. **Access Admin Panel**: Navigate to `/admin` or `/login`
2. **Login**: Use your admin credentials to authenticate
3. **Manage Articles**: 
   - Create new articles with title, slug, tags, and markdown content
   - Edit existing articles
   - Delete articles
   - View all articles in a table format

### Creating Articles

When creating or editing articles:

1. **Title**: The display title of the article
2. **Slug**: URL-friendly identifier (used in URLs)
3. **Tags**: Comma-separated tags for categorization
4. **Content File**: Upload a `.md` file containing the article content

## API Integration

The frontend communicates with the Spring Boot backend using these endpoints:

- `GET /api/v1/articles/metadata` - Fetch articles with pagination
- `GET /api/v1/articles/content/{id}` - Fetch article content
- `POST /api/v1/articles` - Create new article (admin only)
- `PUT /api/v1/articles/{id}` - Update article (admin only)
- `DELETE /api/v1/articles/{id}` - Delete article (admin only)
- `POST /api/v1/auth/login` - Admin authentication

## Project Structure

```
src/
├── components/          # Reusable components
│   └── ArticleModal.jsx # Modal for creating/editing articles
├── contexts/            # React contexts
│   └── AuthContext.jsx  # Authentication context
├── pages/              # Page components
│   ├── HomePage.jsx    # Main articles listing
│   ├── ArticlePage.jsx # Individual article view
│   ├── AdminPage.jsx   # Admin panel
│   └── LoginPage.jsx   # Admin login
├── services/           # API services
│   └── api.js         # Axios configuration and API calls
├── App.jsx            # Main app component
├── App.css           # Global styles
└── main.jsx          # App entry point
```

## Styling

The application uses modern CSS with:
- CSS Grid for responsive layouts
- Flexbox for component alignment
- CSS custom properties for consistent theming
- Hover effects and smooth transitions
- Mobile-first responsive design

## Development

### Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

### Environment Configuration

The API base URL is configured in `src/services/api.js`. Update this if your backend runs on a different port or domain.

## Deployment

1. Build the project:
   ```bash
   npm run build
   ```

2. The `dist` folder contains the production-ready files
3. Deploy to your preferred hosting service (Netlify, Vercel, etc.)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is part of the Personal Blog application.