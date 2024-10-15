import React, { useState } from 'react';

function IntegratedCloudConnectForm() {
  const [cloudProvider, setCloudProvider] = useState('AWS');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [connectionStatus, setConnectionStatus] = useState(null);
  const [formData, setFormData] = useState({
    driverName: '',
    providerName: '',
    driverLibFileName: '',
    credentialName: '',
    regionName: '',
    regionKey: '',
    regionValue: '',
    zoneKey: '',
    zoneValue: '',
    configName: ''
  });

  const [awsSpecificData, setAwsSpecificData] = useState({
    credentialAccessKey: '',
    credentialAccessKeyVal: '',
    credentialSecretKey: '',
    credentialSecretKeyVal: '',
  });

  const [azureSpecificData, setAzureSpecificData] = useState({
    clientIdKey: '',
    clientIdValue: '',
    clientSecretKey: '',
    clientSecretValue: '',
    tenantIdKey: '',
    tenantIdValue: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  const handleProviderSpecificChange = (e) => {
    const { name, value } = e.target;
    if (cloudProvider === 'AWS') {
      setAwsSpecificData(prevState => ({ ...prevState, [name]: value }));
    } else if (cloudProvider === 'AZURE') {
      setAzureSpecificData(prevState => ({ ...prevState, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);
    setConnectionStatus(null);

    const userId = parseInt(localStorage.getItem('userId'), 10);
    let dataToSend = {
      userId,
      ...formData,
      ...(cloudProvider === 'AWS' ? awsSpecificData : azureSpecificData)
    };

    try {
      // Step 1: Create cloud configuration
      const createResponse = await fetch(`http://192.168.20.38:8080/api/cloud/${cloudProvider.toLowerCase()}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!createResponse.ok) {
        throw new Error('Failed to create cloud configuration');
      }

      const createResult = await createResponse.json();
      console.log('Cloud configuration created:', createResult);

      // Step 2: Connect to cloud provider
      const connectResponse = await fetch(`http://192.168.20.38:8080/api/spider/${cloudProvider.toLowerCase()}/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!connectResponse.ok) {
        throw new Error('Failed to connect to cloud provider');
      }

      const connectResult = await connectResponse.text();
      console.log('Connection result:', connectResult);
      setConnectionStatus(`Successfully connected to ${cloudProvider}`);
    } catch (error) {
      console.error('Error:', error);
      setError(`Failed to set up and connect to ${cloudProvider}. Please try again.`);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)} disabled={isSubmitting}>
        <option value="AWS">AWS</option>
        <option value="AZURE">Azure</option>
        <option value="OPENSTACK">Openstack</option>
      </select>

      <input type="text" name="driverName" value={formData.driverName} onChange={handleChange} placeholder="Driver Name" required />
      <input type="text" name="providerName" value={formData.providerName} onChange={handleChange} placeholder="Provider Name" required />
      <input type="text" name="driverLibFileName" value={formData.driverLibFileName} onChange={handleChange} placeholder="Driver Lib File Name" required />
      <input type="text" name="credentialName" value={formData.credentialName} onChange={handleChange} placeholder="Credential Name" required />
      <input type="text" name="regionName" value={formData.regionName} onChange={handleChange} placeholder="Region Name" required />
      <input type="text" name="regionKey" value={formData.regionKey} onChange={handleChange} placeholder="Region Key" required />
      <input type="text" name="regionValue" value={formData.regionValue} onChange={handleChange} placeholder="Region Value" required />
      <input type="text" name="zoneKey" value={formData.zoneKey} onChange={handleChange} placeholder="Zone Key" required />
      <input type="text" name="zoneValue" value={formData.zoneValue} onChange={handleChange} placeholder="Zone Value" required />
      <input type="text" name="configName" value={formData.configName} onChange={handleChange} placeholder="Config Name" required />

      {cloudProvider === 'AWS' && (
        <>
          <input type="text" name="credentialAccessKey" value={awsSpecificData.credentialAccessKey} onChange={handleProviderSpecificChange} placeholder="Credential Access Key" required />
          <input type="text" name="credentialAccessKeyVal" value={awsSpecificData.credentialAccessKeyVal} onChange={handleProviderSpecificChange} placeholder="Credential Access Key Value" required />
          <input type="text" name="credentialSecretKey" value={awsSpecificData.credentialSecretKey} onChange={handleProviderSpecificChange} placeholder="Credential Secret Key" required />
          <input type="text" name="credentialSecretKeyVal" value={awsSpecificData.credentialSecretKeyVal} onChange={handleProviderSpecificChange} placeholder="Credential Secret Key Value" required />
        </>
      )}

      {cloudProvider === 'AZURE' && (
        <>
          <input type="text" name="clientIdKey" value={azureSpecificData.clientIdKey} onChange={handleProviderSpecificChange} placeholder="Client Id Key" required />
          <input type="text" name="clientIdValue" value={azureSpecificData.clientIdValue} onChange={handleProviderSpecificChange} placeholder="Client Id Value" required />
          <input type="text" name="clientSecretKey" value={azureSpecificData.clientSecretKey} onChange={handleProviderSpecificChange} placeholder="Client Secret Key" required />
          <input type="text" name="clientSecretValue" value={azureSpecificData.clientSecretValue} onChange={handleProviderSpecificChange} placeholder="Client Secret Value" required />
          <input type="text" name="tenantIdKey" value={azureSpecificData.tenantIdKey} onChange={handleProviderSpecificChange} placeholder="Tenant Id Key" required />
          <input type="text" name="tenantIdValue" value={azureSpecificData.tenantIdValue} onChange={handleProviderSpecificChange} placeholder="Tenant Id Value" required />
        </>
      )}

      <button type="submit" className="action-button" disabled={isSubmitting}>
        {isSubmitting ? 'Connecting...' : 'Create Config and Connect'}
      </button>

      {error && <p className="error-message">{error}</p>}
      {connectionStatus && <p className="success-message">{connectionStatus}</p>}
    </form>
  );
}

export default IntegratedCloudConnectForm;