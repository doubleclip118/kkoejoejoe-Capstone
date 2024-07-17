import React, { useState } from 'react';
import './App.css';

function CloudInfo() {
  const [selectedCloud, setSelectedCloud] = useState('AWS');
  const [awsInfo, setAwsInfo] = useState({ accessKey: '', secretKey: '', region: '' });
  const [gcpInfo, setGcpInfo] = useState({ projectId: '', clientEmail: '', privateKey: '' });
  const [azureInfo, setAzureInfo] = useState({ clientId: '', clientSecret: '', tenantId: '' });
  const [successMessage, setSuccessMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://43.203.255.53:8080/api/cloudinfo', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ awsInfo, gcpInfo, azureInfo }),
      });

      if (!response.ok) {
        throw new Error('Failed to submit cloud info');
      }

      const data = await response.json();
      console.log('Cloud info submitted successfully:', data);
      setSuccessMessage('Cloud info submitted successfully!');
      setError('');
    } catch (error) {
      setError('Submission failed: ' + error.message);
      setSuccessMessage('');
    }
  };

  return (
    <div className="App">
      <div className="card">
        <h2>Enter Cloud Provider Details</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Select Cloud Service: </label>
            <select value={selectedCloud} onChange={(e) => setSelectedCloud(e.target.value)}>
              <option value="AWS">AWS</option>
              <option value="GCP">GCP</option>
              <option value="Azure">Azure</option>
            </select>
          </div>
          
          {selectedCloud === 'AWS' && (
            <>
              <h3>AWS</h3>
              <input
                type="text"
                placeholder="Access Key"
                value={awsInfo.accessKey}
                onChange={(e) => setAwsInfo({ ...awsInfo, accessKey: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Secret Key"
                value={awsInfo.secretKey}
                onChange={(e) => setAwsInfo({ ...awsInfo, secretKey: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Region"
                value={awsInfo.region}
                onChange={(e) => setAwsInfo({ ...awsInfo, region: e.target.value })}
                required
              />
            </>
          )}

          {selectedCloud === 'GCP' && (
            <>
              <h3>GCP</h3>
              <input
                type="text"
                placeholder="Project ID"
                value={gcpInfo.projectId}
                onChange={(e) => setGcpInfo({ ...gcpInfo, projectId: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Client Email"
                value={gcpInfo.clientEmail}
                onChange={(e) => setGcpInfo({ ...gcpInfo, clientEmail: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Private Key"
                value={gcpInfo.privateKey}
                onChange={(e) => setGcpInfo({ ...gcpInfo, privateKey: e.target.value })}
                required
              />
            </>
          )}

          {selectedCloud === 'Azure' && (
            <>
              <h3>Azure</h3>
              <input
                type="text"
                placeholder="Client ID"
                value={azureInfo.clientId}
                onChange={(e) => setAzureInfo({ ...azureInfo, clientId: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Client Secret"
                value={azureInfo.clientSecret}
                onChange={(e) => setAzureInfo({ ...azureInfo, clientSecret: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Tenant ID"
                value={azureInfo.tenantId}
                onChange={(e) => setAzureInfo({ ...azureInfo, tenantId: e.target.value })}
                required
              />
            </>
          )}

          {error && <p className="error">{error}</p>}
          {successMessage && <p className="success">{successMessage}</p>}
          <button type="submit">Submit</button>
        </form>
      </div>
    </div>
  );
}

export default CloudInfo;
