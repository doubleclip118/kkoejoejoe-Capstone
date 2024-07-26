import React, { useState } from "react";

function ViewConnection() {
  const [cloudProvider, setCloudProvider] = useState("AWS");
  const [isConnecting, setIsConnecting] = useState(false);
  const [error, setError] = useState(null);
  const [connectionStatus, setConnectionStatus] = useState(null);

  const connectToCloudProvider = async () => {
    setIsConnecting(true);
    setError(null);
    setConnectionStatus(null);

    try {
      const response = await fetch(`http://3.34.135.215:8080/api/cloud/${cloudProvider.toLowerCase()}/connect`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId: localStorage.getItem('userId') }),
      });

      if (!response.ok) {
        throw new Error('Failed to connect to cloud provider');
      }

      const result = await response.json();

      // Check if the response includes userId to determine successful connection
      if (result.userId) {
        setConnectionStatus(`Successfully connected to ${cloudProvider}`);
      } else {
        throw new Error('Invalid response from server');
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setIsConnecting(false);
    }
  };

  return (
    <div className="cloud-info-view">
      <h3>View Cloud Connection</h3>
      <select
        value={cloudProvider}
        onChange={(e) => setCloudProvider(e.target.value)}
        disabled={isConnecting}
      >
        <option value="AWS">AWS</option>
        <option value="AZURE">Azure</option>
        <option value="OPENSTACK">Openstack</option>
      </select>
      <button
        onClick={connectToCloudProvider}
        className="action-button view-connection-button"
        disabled={isConnecting}
      >
        {isConnecting ? 'Viewing...' : 'View Cloud Connection'}
      </button>
      {error && <p className="error-message">{error}</p>}
      {connectionStatus && <p className="success-message">{connectionStatus}</p>}
    </div>
  );
}

export default ViewConnection;
