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
            const userId = localStorage.getItem('userId');

            const response = await fetch(`http://192.168.20.38:8080/api/spider/${cloudProvider.toLowerCase()}/${userId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error('Failed to connect');
            }

            // 응답이 JSON인지 아닌지를 확인 후 처리
            const contentType = response.headers.get("content-type");
            let result;
            
            if (contentType && contentType.includes("application/json")) {
                result = await response.json();
            } else {
                result = await response.text();  // JSON이 아닌 경우 텍스트로 처리
            }

            console.log('Connection result:', result);
            setConnectionStatus(result === "생성 완료" ? `Successfully connected to ${cloudProvider}` : result);
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
                <option value="OPENSTACK">Openstack</option>
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
