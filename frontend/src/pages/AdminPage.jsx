import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { articlesApi } from '../services/api';
import ArticleModal from '../components/ArticleModal';
import ConfirmModal from '../components/ConfirmModal';
import { Plus, Edit, Trash2, LogOut } from 'lucide-react';

const AdminPage = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingArticle, setEditingArticle] = useState(null);
  const [confirmModal, setConfirmModal] = useState({ open: false, articleId: null, title: '' });
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchArticles();
  }, [isAuthenticated, navigate]);

  const fetchArticles = async () => {
    try {
      setLoading(true);
      const response = await articlesApi.getArticlesMetadata(0, 1000);
      setArticles(response.data.content);
    } catch (error) {
      console.error('Error fetching articles:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateArticle = () => {
    setEditingArticle(null);
    setModalOpen(true);
  };

  const handleEditArticle = (article) => {
    setEditingArticle(article);
    setModalOpen(true);
  };

  const handleDeleteArticle = (article) => {
    setConfirmModal({
      open: true,
      articleId: article.id,
      title: article.title
    });
  };

  const confirmDelete = async () => {
    try {
      await articlesApi.deleteArticle(confirmModal.articleId);
      setArticles(articles.filter(article => article.id !== confirmModal.articleId));
      setConfirmModal({ open: false, articleId: null, title: '' });
    } catch (error) {
      console.error('Error deleting article:', error);
    }
  };

  const cancelDelete = () => {
    setConfirmModal({ open: false, articleId: null, title: '' });
  };

  const handleModalClose = () => {
    setModalOpen(false);
    setEditingArticle(null);
  };

  const handleArticleSaved = () => {
    fetchArticles();
    handleModalClose();
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  if (!isAuthenticated) {
    return null;
  }

  if (loading) {
    return <div className="loading">Loading admin panel...</div>;
  }

  return (
    <div className="admin-panel">
      <div className="admin-header">
        <h1>Admin Panel</h1>
        <div style={{ display: 'flex', gap: '1rem' }}>
          <button className="btn" onClick={handleCreateArticle}>
            <Plus size={16} />
            New Article
          </button>
          <button className="btn btn-secondary" onClick={handleLogout}>
            <LogOut size={16} />
            Logout
          </button>
        </div>
      </div>

      {articles.length === 0 ? (
        <div className="loading">No articles found.</div>
      ) : (
        <table className="admin-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Slug</th>
              <th>Tags</th>
              <th>Created</th>
              <th>Updated</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {articles.map((article) => (
              <tr key={article.id}>
                <td>{article.title}</td>
                <td>{article.slug}</td>
                <td>
                  {article.tags && Array.from(article.tags).map(tag => (
                    <span key={tag} className="tag" style={{ marginRight: '0.25rem' }}>
                      {tag}
                    </span>
                  ))}
                </td>
                <td>{formatDate(article.createdAt)}</td>
                <td>{article.updatedAt ? formatDate(article.updatedAt) : ''}</td>
                <td>
                  <div className="actions">
                    <button
                      className="btn"
                      onClick={() => handleEditArticle(article)}
                      style={{ padding: '0.5rem' }}
                    >
                      <Edit size={14} />
                    </button>
                    <button
                      className="btn btn-danger"
                      onClick={() => handleDeleteArticle(article)}
                      style={{ padding: '0.5rem' }}
                    >
                      <Trash2 size={14} />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {modalOpen && (
        <ArticleModal
          article={editingArticle}
          onClose={handleModalClose}
          onSave={handleArticleSaved}
        />
      )}

      <ConfirmModal
        isOpen={confirmModal.open}
        title="Delete Article"
        message={`Are you sure you want to delete "${confirmModal.title}"? This action cannot be undone.`}
        onConfirm={confirmDelete}
        onCancel={cancelDelete}
      />
    </div>
  );
};

export default AdminPage;