import React, { useState } from "react";

function DeleteConnection() {
  const [cloudProvider, setCloudProvider] = useState("AWS");
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState(null);
  const [connectionStatus, setConnectionStatus] = useState(null);

  const deleteCloudProvider = async () => {
    setIsDeleting(true);
    setError(null);
    setConnectionStatus(null);

    try {
      const response = await fetch(`http://3.34.135.215:8080/api/cloud/${cloudProvider.toLowerCase()}/delete`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId: localStorage.getItem('userId') }),
      });

      if (!response.ok) {
        throw new Error('Failed to delete cloud provider connection');
      }

      const result = await response.json();

      // Check if the response includes userId to determine successful deletion
      if (result.userId) {
        setConnectionStatus(`Successfully deleted connection to ${cloudProvider}`);
        // Reset local storage flag based on the cloud provider
        if (cloudProvider === 'AWS') {
          localStorage.setItem('aws_is_available', 'false');
        } else if (cloudProvider === 'AZURE') {
          localStorage.setItem('azure_is_available', 'false');
        }
      } else {
        throw new Error('Invalid response from server');
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="cloud-info-view">
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
        onClick={deleteCloudProvider}
        className="action-button view-connection-button"
        disabled={isDeleting}
      >
        {isDeleting ? 'Deleting...' : 'Delete Cloud Connection'}
      </button>
      {error && <p className="error-message">{error}</p>}
      {connectionStatus && <p className="success-message">{connectionStatus}</p>}
    </div>
  );
}

export default DeleteConnection;
