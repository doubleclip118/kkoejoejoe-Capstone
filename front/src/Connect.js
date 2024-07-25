import React, { useState } from "react";

function Connect() {
    const [cloudProvider, setCloudProvider] = useState('AWS');
    const [isConnecting, setIsConnecting] = useState(false);
    const [error, setError] = useState(null);
    const [connectionStatus, setConnectionStatus] = useState(null);

    const connectToCloudProvider = async () => {
        setIsConnecting(true);
        setError(null);
        setConnectionStatus(null);

        try {
            const response = await fetch('http://3.34.135.215:8080/api/cloud/connect', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    provider: cloudProvider,
                    userId: parseInt(localStorage.getItem('userId'), 10)
                })
            });

            if (!response.ok) {
                throw new Error('Failed to connect');
            }

            const result = await response.json();
            setConnectionStatus(`Successfully connected to ${cloudProvider}`);
            console.log('Connection result:', result);
        } catch (error) {
            console.error('Error:', error);
            setError(`Failed to connect to ${cloudProvider}. Please try again.`);
        } finally {
            setIsConnecting(false);
        }
    };

    return (
        <div className="cloud-info-connect">
            <h3>Connect to Cloud Provider</h3>
            <select 
                value={cloudProvider} 
                onChange={(e) => setCloudProvider(e.target.value)}
                disabled={isConnecting}
            >
                <option value="AWS">AWS</option>
                <option value="AZURE">Azure</option>
            </select>
            <button 
                onClick={connectToCloudProvider} 
                className="action-button connect-button"
                disabled={isConnecting}
            >
                {isConnecting ? 'Connecting...' : 'Connect to Cloud'}
            </button>
            {error && <p className="error-message">{error}</p>}
            {connectionStatus && <p className="success-message">{connectionStatus}</p>}
        </div>
    );
}

export default Connect;