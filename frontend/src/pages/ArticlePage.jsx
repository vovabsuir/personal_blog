import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import { articlesApi } from '../services/api';
import { ArrowLeft } from 'lucide-react';

const ArticlePage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [article, setArticle] = useState(null);
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchArticle();
  }, [id]);

  const fetchArticle = async () => {
    try {
      setLoading(true);
      setError(null);

      const metadataResponse = await articlesApi.getArticlesMetadata(0, 1000);
      const articleMetadata = metadataResponse.data.content.find(a => a.id === id);
      
      if (!articleMetadata) {
        setError('Article not found');
        return;
      }

      const contentResponse = await articlesApi.getArticleContent(id);
      
      setArticle(articleMetadata);
      setContent(contentResponse.data.content);
    } catch (error) {
      console.error('Error fetching article:', error);
      setError('Failed to load article');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  if (loading) {
    return <div className="loading">Loading article...</div>;
  }

  if (error) {
    return (
      <div className="container">
        <button className="back-button" onClick={() => navigate('/')}>
          <ArrowLeft size={16} />
          Back to Articles
        </button>
        <div className="error-message">{error}</div>
      </div>
    );
  }

  return (
    <div className="container">
      <button className="back-button" onClick={() => navigate('/')}>
        <ArrowLeft size={16} />
        Back to Articles
      </button>

      <article className="article-content">
        <h1>{article.title}</h1>
        
        <div className="meta">
          <div className="article-meta">
            <span>Published: {formatDate(article.createdAt)}</span>
            {article.updatedAt != null && (
              <span>Updated: {formatDate(article.updatedAt)}</span>
            )}
          </div>
          
          {article.tags && article.tags.length > 0 && (
            <div className="article-tags">
              {Array.from(article.tags).map((tag) => (
                <span key={tag} className="tag">
                  {tag}
                </span>
              ))}
            </div>
          )}
        </div>

        <div className="markdown-content">
          <ReactMarkdown>{content}</ReactMarkdown>
        </div>
      </article>
    </div>
  );
};

export default ArticlePage;