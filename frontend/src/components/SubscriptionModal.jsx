import { useState } from 'react';
import { subscriptionApi } from '../services/api';
import { X, Mail } from 'lucide-react';

const SubscriptionModal = ({ isOpen, onClose }) => {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await subscriptionApi.subscribe(email);
      setSuccess(true);
      setEmail('');
    } catch (error) {
      setError(error.response?.data?.message || 'Failed to subscribe');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setEmail('');
    setError('');
    setSuccess(false);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal">
      <div className="modal-content" style={{ maxWidth: '400px' }}>
        <div className="modal-header">
          <h2>Subscribe to Newsletter</h2>
          <button className="close-button" onClick={handleClose}>
            <X size={20} />
          </button>
        </div>

        {success ? (
          <div className="success-message">
            <Mail size={24} style={{ marginBottom: '1rem' }} />
            <p>Thank you for subscribing!</p>
            <p style={{ fontSize: '0.875rem', color: '#666', marginTop: '0.5rem' }}>
              Please check your email to confirm your subscription.
            </p>
            <button className="btn" onClick={handleClose} style={{ marginTop: '1rem' }}>
              Close
            </button>
          </div>
        ) : (
          <>
            <p style={{ marginBottom: '1.5rem', color: '#666' }}>
              Get notified when new articles are published.
            </p>

            {error && <div className="error-message">{error}</div>}

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="email">Email Address</label>
                <input
                  type="email"
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="your@email.com"
                  required
                  disabled={loading}
                />
              </div>

              <div className="form-actions">
                <button type="button" className="btn btn-secondary" onClick={handleClose} disabled={loading}>
                  Cancel
                </button>
                <button type="submit" className="btn" disabled={loading || !email.trim()}>
                  {loading ? 'Subscribing...' : 'Subscribe'}
                </button>
              </div>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default SubscriptionModal;