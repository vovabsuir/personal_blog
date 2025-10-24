import { useState, useEffect } from 'react';
import { articlesApi } from '../services/api';
import { X } from 'lucide-react';

const ArticleModal = ({ article, onClose, onSave }) => {
  const [formData, setFormData] = useState({
    title: '',
    slug: '',
    tags: '',
  });
  const [contentFile, setContentFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (article) {
      setFormData({
        title: article.title || '',
        slug: article.slug || '',
        tags: article.tags ? Array.from(article.tags).join(', ') : '',
      });
    }
  }, [article]);

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleFileChange = (e) => {
    setContentFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const formDataToSend = new FormData();
      
      const metadata = {
        title: formData.title,
        slug: formData.slug,
        tags: formData.tags.split(',').map(tag => tag.trim()).filter(tag => tag)
      };
      
      formDataToSend.append('metadata', new Blob([JSON.stringify(metadata)], {
        type: 'application/json'
      }));

      if (contentFile) {
        formDataToSend.append('content', contentFile);
      }

      if (article) {
        await articlesApi.updateArticle(article.id, formDataToSend);
      } else {
        if (!contentFile) {
          setError('Content file is required for new articles');
          setLoading(false);
          return;
        }
        await articlesApi.createArticle(formDataToSend);
      }

      onSave();
    } catch (error) {
      console.error('Error saving article:', error);
      setError(error.response?.data?.message || 'Failed to save article');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal">
      <div className="modal-content">
        <div className="modal-header">
          <h2>{article ? 'Edit Article' : 'Create New Article'}</h2>
          <button className="close-button" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="title">Title *</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="slug">Slug *</label>
            <input
              type="text"
              id="slug"
              name="slug"
              value={formData.slug}
              onChange={handleInputChange}
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="tags">Tags (comma-separated)</label>
            <input
              type="text"
              id="tags"
              name="tags"
              value={formData.tags}
              onChange={handleInputChange}
              placeholder="react, javascript, tutorial"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="content">
              Content File (.md) {article ? '(optional - leave empty to keep current content)' : '*'}
            </label>
            <input
              type="file"
              id="content"
              accept=".md,.txt"
              onChange={handleFileChange}
              required={!article}
              disabled={loading}
              style={{ display: 'none' }}
            />
            <label htmlFor="content" className="custom-file-label">
              {contentFile ? contentFile.name : 'Click to select file'}
            </label>
          </div>

          <div className="form-actions">
            <button type="button" className="btn btn-secondary" onClick={onClose} disabled={loading}>
              Cancel
            </button>
            <button type="submit" className="btn" disabled={loading}>
              {loading ? 'Saving...' : (article ? 'Update' : 'Create')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ArticleModal;