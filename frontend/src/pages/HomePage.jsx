import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { articlesApi } from '../services/api';
import SubscriptionModal from '../components/SubscriptionModal';
import { ChevronLeft, ChevronRight, Search, Tag, X, Bell } from 'lucide-react';

const HomePage = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searching, setSearching] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchTitle, setSearchTitle] = useState('');
  const [selectedTags, setSelectedTags] = useState([]);
  const [allTags, setAllTags] = useState([]);
  const [showTagFilter, setShowTagFilter] = useState(false);
  const [subscriptionModalOpen, setSubscriptionModalOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchArticles(currentPage);
  }, [currentPage, selectedTags]);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      setCurrentPage(0);
      fetchArticles(0);
    }, 300);

    return () => clearTimeout(timeoutId);
  }, [searchTitle]);

  useEffect(() => {
    fetchAllTags();
  }, []);

  const fetchArticles = async (page) => {
    try {
      const isInitialLoad = articles.length === 0;
      if (isInitialLoad) {
        setLoading(true);
      } else {
        setSearching(true);
      }
      
      let response;
      
      if (searchTitle.trim()) {
        response = await articlesApi.searchArticlesByTitle(searchTitle.trim(), page);
      } else if (selectedTags.length > 0) {
        response = await articlesApi.getArticlesByTags(selectedTags, page);
      } else {
        response = await articlesApi.getArticlesMetadata(page);
      }
      
      setArticles(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('Error fetching articles:', error);
    } finally {
      setLoading(false);
      setSearching(false);
    }
  };

  const fetchAllTags = async () => {
    try {
      const response = await articlesApi.getAllTags();
      setAllTags(Array.from(response.data).sort());
    } catch (error) {
      console.error('Error fetching tags:', error);
    }
  };

  const handleArticleClick = (id) => {
    navigate(`/article/${id}`);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
  };

  const handleTagToggle = (tag) => {
    setSearchTitle('');
    setSelectedTags(prev => {
      const newTags = prev.includes(tag) 
        ? prev.filter(t => t !== tag)
        : [...prev, tag];
      setCurrentPage(0);
      return newTags;
    });
  };

  const clearFilters = () => {
    setSearchTitle('');
    setSelectedTags([]);
    setCurrentPage(0);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  return (
    <div className="container">
      <header className="header">
        <div className="header-content">
          <h1>Personal Blog</h1>
          <button 
            className="subscription-button"
            onClick={() => setSubscriptionModalOpen(true)}
            title="Subscribe to newsletter"
          >
            <Bell size={20} />
          </button>
        </div>
      </header>

      <div className="search-filters">
        <form className="search-form" onSubmit={handleSearchSubmit}>
          <div className="search-input-wrapper">
            <Search size={20} className="search-icon" />
            <input
              type="text"
              placeholder="Search articles by title..."
              value={searchTitle}
              onChange={(e) => setSearchTitle(e.target.value)}
              className="search-input"
            />
          </div>
        </form>

        <div className="filter-section">
          <button 
            className="filter-toggle"
            onClick={() => setShowTagFilter(!showTagFilter)}
          >
            <Tag size={16} />
            Filter by Tags
          </button>
          
          {(searchTitle || selectedTags.length > 0) && (
            <button className="clear-filters" onClick={clearFilters}>
              <X size={16} />
              Clear Filters
            </button>
          )}
        </div>

        {showTagFilter && (
          <div className="tag-filter">
            <div className="available-tags">
              {allTags.map(tag => (
                <button
                  key={tag}
                  className={`tag-button ${selectedTags.includes(tag) ? 'selected' : ''}`}
                  onClick={() => handleTagToggle(tag)}
                >
                  {tag}
                </button>
              ))}
            </div>
          </div>
        )}

        {selectedTags.length > 0 && (
          <div className="active-filters">
            <span>Active filters:</span>
            {selectedTags.map(tag => (
              <span key={tag} className="active-tag">
                {tag}
                <button onClick={() => handleTagToggle(tag)}>
                  <X size={12} />
                </button>
              </span>
            ))}
          </div>
        )}
      </div>

      {articles.length === 0 && !searching ? (
        <div className="loading">No articles found.</div>
      ) : (
        <>
          <div className="articles-grid">
            {articles.map((article) => (
              <div
                key={article.id}
                className="article-card"
                onClick={() => handleArticleClick(article.id)}
              >
                <h3>{article.title}</h3>
                <div className="article-meta">
                  <span>{formatDate(article.createdAt)}</span>
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
            ))}
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button
                onClick={() => setCurrentPage(currentPage - 1)}
                disabled={currentPage === 0}
              >
                <ChevronLeft size={16} />
                Previous
              </button>
              
              <span>
                Page {currentPage + 1} of {totalPages}
              </span>
              
              <button
                onClick={() => setCurrentPage(currentPage + 1)}
                disabled={currentPage === totalPages - 1}
              >
                Next
                <ChevronRight size={16} />
              </button>
            </div>
          )}
        </>
      )}

      <SubscriptionModal
        isOpen={subscriptionModalOpen}
        onClose={() => setSubscriptionModalOpen(false)}
      />
    </div>
  );
};

export default HomePage;