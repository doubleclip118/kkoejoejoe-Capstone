import React, { useState, useEffect } from 'react';
import './Main.css'; // 필요한 스타일을 Main.css에서 관리

function CloudInfo() {
  const [menu, setMenu] = useState('view');
  const [cloudProvider, setCloudProvider] = useState('AWS');
  const [selectedCloudInfo, setSelectedCloudInfo] = useState(null);
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

  const [existingCloudInfo, setExistingCloudInfo] = useState([]);

  useEffect(() => {
    fetchExistingCloudInfo();
  }, []);

  const fetchExistingCloudInfo = async () => {
    try {
      const response = await fetch('http://3.34.135.215:8080/api/cloud');
      if (!response.ok) {
        throw new Error('Failed to fetch cloud info');
      }
      const data = await response.json();
      setExistingCloudInfo(data);
    } catch (error) {
      console.error('Error fetching cloud info:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  const handleProviderSpecificChange = (e) => {
    const { name, value } = e.target;
    if (cloudProvider === 'AWS') {
      setAwsSpecificData(prevState => ({ ...prevState, [name]: value }));
    } else {
      setAzureSpecificData(prevState => ({ ...prevState, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    let dataToSend = {
      userId: localStorage.getItem('userId'), // Get userId from localStorage
      driverName: formData.driverName,
      providerName: formData.providerName,
      driverLibFileName: formData.driverLibFileName,
      credentialName: formData.credentialName,
      regionName: formData.regionName,
      regionKey: formData.regionKey,
      regionValue: formData.regionValue,
      zoneKey: formData.zoneKey,
      zoneValue: formData.zoneValue,
      ...(cloudProvider === 'AWS' ? awsSpecificData : azureSpecificData)
    };

    try {
      const response = await fetch(`http://3.34.135.215:8080/api/cloud/aws`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const result = await response.json();
      console.log('Success:', result);
      fetchExistingCloudInfo();  // Refresh the list after adding new info
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleDelete = async () => {
    try {
      const response = await fetch(`http://your-api-endpoint/cloud-info/${cloudProvider}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Failed to delete cloud info');
      }

      const result = await response.json();
      console.log('Success:', result);
      fetchExistingCloudInfo();  // Refresh the list after deleting info
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    let dataToSend = {
      userId: localStorage.getItem('userId'), // Get userId from localStorage
      DriverName: formData.DriverName,
      ProviderName: formData.ProviderName,
      DriverLibFileName: formData.DriverLibFileName,
      CredentialName: formData.CredentialName,
      RegionName: formData.RegionName,
      RegionKey: formData.RegionKey,
      RegionValue: formData.RegionValue,
      ZoneKey: formData.ZoneKey,
      ZoneValue: formData.ZoneValue,
      ...(cloudProvider === 'AWS' ? awsSpecificData : azureSpecificData)
    };

    try {
      const response = await fetch(`http://your-api-endpoint/cloud-info/${cloudProvider.toLowerCase()}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error('Failed to update cloud info');
      }

      const result = await response.json();
      console.log('Success:', result);
      fetchExistingCloudInfo();  // Refresh the list after updating info
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const renderExistingCloudInfo = () => {
    return (
      <div className="existing-cloud-info">
        <h3>Existing Cloud Information</h3>
        {existingCloudInfo.map((info, index) => (
          <div key={index} className="cloud-info-item">
            <h4>{info.ProviderName}</h4>
            <p>Driver Name: {info.DriverName}</p>
            <p>Credential Name: {info.CredentialName}</p>
            <p>Region: {info.RegionValue}</p>
            <p>Zone: {info.ZoneValue}</p>
          </div>
        ))}
      </div>
    );
  };

  const renderForm = (handleSubmit, actionLabel) => {
    return (
      <form onSubmit={handleSubmit}>
        <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)}>
          <option value="AWS">AWS</option>
          <option value="AZURE">Azure</option>
        </select>

        {/* Common fields */}
        <input type="text" name="DriverName" value={formData.DriverName} onChange={handleChange} placeholder="Driver Name" required />
        <input type="text" name="ProviderName" value={formData.ProviderName} onChange={handleChange} placeholder="Provider Name" required />
        <input type="text" name="DriverLibFileName" value={formData.DriverLibFileName} onChange={handleChange} placeholder="Driver Lib File Name" required />
        <input type="text" name="CredentialName" value={formData.CredentialName} onChange={handleChange} placeholder="Credential Name" required />
        <input type="text" name="RegionName" value={formData.RegionName} onChange={handleChange} placeholder="Region Name" required />
        <input type="text" name="RegionKey" value={formData.RegionKey} onChange={handleChange} placeholder="Region Key" required />
        <input type="text" name="RegionValue" value={formData.RegionValue} onChange={handleChange} placeholder="Region Value" required />
        <input type="text" name="ZoneKey" value={formData.ZoneKey} onChange={handleChange} placeholder="Zone Key" required />
        <input type="text" name="ZoneValue" value={formData.ZoneValue} onChange={handleChange} placeholder="Zone Value" required />

        {/* AWS specific fields */}
        {cloudProvider === 'AWS' && (
          <>
            <input type="text" name="CredentialAccessKey" value={awsSpecificData.CredentialAccessKey} onChange={handleProviderSpecificChange} placeholder="Credential Access Key" required />
            <input type="text" name="CredentialAccessKeyVal" value={awsSpecificData.CredentialAccessKeyVal} onChange={handleProviderSpecificChange} placeholder="Credential Access Key Value" required />
            <input type="text" name="CredentialSecretKey" value={awsSpecificData.CredentialSecretKey} onChange={handleProviderSpecificChange} placeholder="Credential Secret Key" required />
            <input type="text" name="CredentialSecretKeyVal" value={awsSpecificData.CredentialSecretKeyVal} onChange={handleProviderSpecificChange} placeholder="Credential Secret Key Value" required />
          </>
        )}

        {/* Azure specific fields */}
        {cloudProvider === 'AZURE' && (
          <>
            <input type="text" name="ClientIdKey" value={azureSpecificData.ClientIdKey} onChange={handleProviderSpecificChange} placeholder="Client Id Key" required />
            <input type="text" name="ClientIdValue" value={azureSpecificData.ClientIdValue} onChange={handleProviderSpecificChange} placeholder="Client Id Value" required />
            <input type="text" name="ClientSecretKey" value={azureSpecificData.ClientSecretKey} onChange={handleProviderSpecificChange} placeholder="Client Secret Key" required />
            <input type="text" name="ClientSecretValue" value={azureSpecificData.ClientSecretValue} onChange={handleProviderSpecificChange} placeholder="Client Secret Value" required />
            <input type="text" name="TenantIdKey" value={azureSpecificData.TenantIdKey} onChange={handleProviderSpecificChange} placeholder="Tenant Id Key" required />
            <input type="text" name="TenantIdValue" value={azureSpecificData.TenantIdValue} onChange={handleProviderSpecificChange} placeholder="Tenant Id Value" required />
          </>
        )}

        <button type="submit" className="action-button">{actionLabel}</button>
      </form>
    );
  };

  return (
    <div className="management-content">
      <h2>Cloud Information Management</h2>
      <nav>
        <button onClick={() => setMenu('view')}>View Cloud Info</button>
        <button onClick={() => setMenu('create')}>Create Cloud Connection</button>
        <button onClick={() => setMenu('delete')}>Delete Cloud Connection</button>
        <button onClick={() => setMenu('update')}>Modify Cloud Info</button>
      </nav>
      
      {menu === 'view' && renderExistingCloudInfo()}

      {menu === 'create' && renderForm(handleSubmit, 'Submit Cloud Info')}

      {menu === 'delete' && (
        <div>
          <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)}>
            <option value="AWS">AWS</option>
            <option value="AZURE">Azure</option>
          </select>
          <button onClick={handleDelete} className="action-button">Delete Cloud Info</button>
        </div>
      )}

      {menu === 'update' && (
        <div>
          <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)}>
            <option value="AWS">AWS</option>
            <option value="AZURE">Azure</option>
          </select>
          {renderForm(handleUpdate, 'Update Cloud Info')}
        </div>
      )}
    </div>
  );
}

export default CloudInfo;
