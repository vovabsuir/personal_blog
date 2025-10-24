import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const articlesApi = {
  getArticlesMetadata: (page = 0, size = 9) =>
    api.get(`/articles/metadata?page=${page}&size=${size}`),
  
  getArticlesByTags: (tags, page = 0, size = 9) =>
    api.get(`/articles/metadata/tags?tags=${tags.join(',')}&page=${page}&size=${size}`),
  
  searchArticlesByTitle: (title, page = 0, size = 9) =>
    api.get(`/articles/metadata/title?title=${encodeURIComponent(title)}&page=${page}&size=${size}`),
  
  getAllTags: () =>
    api.get('/articles/tags'),
  
  getArticleContent: (id) =>
    api.get(`/articles/content/${id}`),
  
  createArticle: (formData) =>
    api.post('/articles', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
  
  updateArticle: (id, formData) =>
    api.put(`/articles/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
  
  deleteArticle: (id) =>
    api.delete(`/articles/${id}`),
};

export const authApi = {
  login: (credentials) =>
    api.post('/auth/login', credentials),
};

export const subscriptionApi = {
  subscribe: (email) =>
    api.post('/subscriptions', { email }),
};

export default api;