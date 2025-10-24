# Frontend Setup Instructions

## Quick Start

1. **Navigate to the frontend directory:**
   ```bash
   cd Frontend/personal-blog-frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

4. **Open your browser and visit:** `http://localhost:5173`

## Backend Requirements

Make sure your Spring Boot backend is running on `http://localhost:8080` before using the frontend.

## Admin Access

To access the admin panel:
1. Navigate to `http://localhost:5173/admin` or `http://localhost:5173/login`
2. Use your backend admin credentials to log in
3. You'll be redirected to the admin panel where you can manage articles

## Testing Article Creation

1. Log in as admin
2. Click "New Article" 
3. Fill in the form:
   - **Title**: "My First Article"
   - **Slug**: "my-first-article"
   - **Tags**: "react, blog, tutorial"
   - **Content File**: Upload the provided `sample-article.md` file
4. Click "Create"

## Features Implemented

✅ **Homepage with Article Cards**
- Modern card-based layout
- Pagination support
- Responsive design
- Click to read full articles
- Search by article title (partial matching)
- Filter by tags with multi-select
- Clear filters functionality

## New Search & Filter Features

**Title Search:**
- Type in the search box to find articles by title
- Supports partial matching (e.g., "react" finds "Introduction to React")
- Real-time search as you type
- Automatically resets to page 1 when searching

**Tag Filtering:**
- Click "Filter by Tags" to see all available tags
- Select multiple tags to filter articles
- Selected tags are highlighted in blue
- Active filters shown below with remove buttons
- Combines with OR logic (articles with any selected tag)

**Filter Management:**
- "Clear Filters" button removes all active filters
- Search and tag filters are mutually exclusive
- Pagination works with filtered results

✅ **Article Reading Page**
- Full markdown rendering
- Article metadata display
- Back navigation
- Clean typography

✅ **Admin Authentication**
- JWT token-based login
- Secure admin routes
- Session persistence

✅ **Admin Panel**
- Article management table
- Create, edit, delete operations
- Modal-based forms
- File upload for markdown content

✅ **Modern UI/UX**
- Minimalist design
- Smooth animations
- Mobile-responsive
- Accessible components

## Troubleshooting

If you encounter build issues:
1. Delete `node_modules` and `package-lock.json`
2. Run `npm install` again
3. Try `npm run dev` instead of build for development

## API Configuration

The frontend is configured to connect to the backend at `http://localhost:8080/api/v1`. 

If your backend runs on a different port, update the `API_BASE_URL` in `src/services/api.js`.