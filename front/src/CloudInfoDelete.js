// CloudInfoDelete.js
import React, { useState } from 'react';

function CloudInfoDelete({ cloudProvider, setCloudProvider, fetchExistingCloudInfo }) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState(null);

  const handleDelete = async () => {
    setIsDeleting(true);
    setError(null);
    const userId = parseInt(localStorage.getItem('userId'))
    try {
      const response = await fetch(`http:/192.168.20.38:80800/api/cloud/${cloudProvider.toLowerCase()}/${userId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error('Failed to delete cloud info');
      }

      const result = await response.json();
      console.log('Success:', result);
      fetchExistingCloudInfo();  // Refresh the list after deleting info
    } catch (error) {
      console.error('Error:', error);
      setError('Failed to delete cloud information. Please try again.');
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="cloud-info-delete">
      <h3>Delete Cloud Connection</h3>
      <select 
        value={cloudProvider} 
        onChange={(e) => setCloudProvider(e.target.value)}
        disabled={isDeleting}
      >
        <option value="AWS">AWS</option>
        <option value="AZURE">Azure</option>
        <option value="OPENSTACK">Openstack</option>
      </select>
      <button 
        onClick={handleDelete} 
        className="action-button delete-button"
        disabled={isDeleting}
      >
        {isDeleting ? 'Deleting...' : 'Delete Cloud Info'}
      </button>
      {error && <p className="error-message">{error}</p>}
    </div>
  );
}

export default CloudInfoDelete;