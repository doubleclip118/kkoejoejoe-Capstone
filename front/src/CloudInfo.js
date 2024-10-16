import React, { useState, useEffect } from 'react';
import './CloudInfo.css';

const cloudProviders = ['AWS', 'AZURE', 'OPENSTACK'];
const userId = parseInt(localStorage.getItem('userId'), 10);

function CloudInfo() {
  const [existingCloudInfo, setExistingCloudInfo] = useState({});
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [activeProvider, setActiveProvider] = useState(null);
  const [isModifying, setIsModifying] = useState(false);
  const [message, setMessage] = useState('');
  const [cloudInfo, setCloudInfo] = useState({});

  useEffect(() => {
    fetchExistingCloudInfo();
  }, []);

  const fetchExistingCloudInfo = async () => {
    setLoading(true);
    try {
      const data = {};
      for (const provider of cloudProviders) {
        const url = `http://192.168.20.38:8080/api/cloud/${provider.toLowerCase()}/${userId}`;
        const response = await fetch(url, { method: 'GET', headers: { 'Content-Type': 'application/json' } });
        if (response.ok) {
          const cloudData = await response.json();
          data[provider] = cloudData;
        } else {
          data[provider] = null;
        }
      }
      console.log('Fetched cloud info:', data);
      setExistingCloudInfo(data);
    } catch (error) {
      console.error('Error fetching cloud info:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = (provider) => {
    setActiveProvider(provider);
    setIsModifying(false);
    setCloudInfo(getInitialCloudInfo(provider));
    setModalVisible(true);
  };

  const handleModify = (provider) => {
    setActiveProvider(provider);
    setIsModifying(true);
    setCloudInfo(existingCloudInfo[provider]);
    setModalVisible(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    const payload = {
      userId: userId,
      ...cloudInfo,
    };

    try {
      const url = `http://192.168.20.38:8080/api/cloud/${activeProvider.toLowerCase()}`;
      const method = isModifying ? 'PUT' : 'POST';
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) throw new Error(`Failed to ${isModifying ? 'modify' : 'create'} cloud info for ${activeProvider}`);

      if (!isModifying) {
        await fetch(`http://192.168.20.38:8080/api/cloud/${activeProvider.toLowerCase()}/${userId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
        });
      }

      setMessage(`Successfully ${isModifying ? 'modified' : 'created'} cloud info for ${activeProvider}`);
      fetchExistingCloudInfo();
      setModalVisible(false);
      setActiveProvider(null);
      setIsModifying(false);
    } catch (error) {
      console.error(`Error ${isModifying ? 'modifying' : 'creating'} cloud info:`, error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (provider) => {
    setLoading(true);
    try {
      const deleteResponse = await fetch(`http://192.168.20.38:8080/api/cloud/${provider.toLowerCase()}/${userId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!deleteResponse.ok) throw new Error(`Failed to delete cloud info for ${provider}`);

      setMessage(`Successfully deleted cloud info for ${provider}`);
      fetchExistingCloudInfo();
    } catch (error) {
      console.error('Error deleting cloud info:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCloudInfo((prev) => ({ ...prev, [name]: value }));
  };

  const getInitialCloudInfo = (provider) => {
    switch (provider) {
      case 'AWS':
        return {
          driverName: '',
          providerName: 'AWS',
          driverLibFileName: '',
          credentialName: '',
          credentialAccessKey: '',
          credentialAccessKeyVal: '',
          credentialSecretKey: '',
          credentialSecretKeyVal: '',
          regionName: '',
          regionKey: '',
          regionValue: '',
          zoneKey: '',
          zoneValue: '',
          configName: '',
        };
      case 'AZURE':
        return {
          driverName: '',
          providerName: 'AZURE',
          driverLibFileName: '',
          credentialName: '',
          clientIdKey: '',
          clientIdValue: '',
          clientSecretKey: '',
          clientSecretValue: '',
          tenantIdKey: '',
          tenantIdValue: '',
          subscriptionIdKey: '',
          subscriptionIdValue: '',
          regionName: '',
          regionKey: '',
          regionValue: '',
          zoneKey: '',
          zoneValue: '',
          configName: '',
        };
      case 'OPENSTACK':
        return {
          driverName: '',
          providerName: 'OPENSTACK',
          driverLibFileName: '',
          credentialName: '',
          identityEndpointKey: '',
          identityEndpointValue: '',
          usernameKey: '',
          usernameValue: '',
          domainNameKey: '',
          domainNameValue: '',
          passwordKey: '',
          passwordValue: '',
          projectIDKey: '',
          projectIDValue: '',
          regionName: '',
          regionKey: '',
          regionValue: '',
        };
      default:
        return {};
    }
  };

  const renderFormFields = () => {
    switch (activeProvider) {
      case 'AWS':
        return (
          <>
            <InputField name="driverName" label="Driver Name" value={cloudInfo.driverName} onChange={handleInputChange} />
            <InputField name="driverLibFileName" label="Driver Lib File Name" value={cloudInfo.driverLibFileName} onChange={handleInputChange} />
            <InputField name="credentialName" label="Credential Name" value={cloudInfo.credentialName} onChange={handleInputChange} />
            <InputField name="credentialAccessKey" label="Credential Access Key" value={cloudInfo.credentialAccessKey} onChange={handleInputChange} />
            <InputField name="credentialAccessKeyVal" label="Credential Access Key Value" value={cloudInfo.credentialAccessKeyVal} onChange={handleInputChange} />
            <InputField name="credentialSecretKey" label="Credential Secret Key" value={cloudInfo.credentialSecretKey} onChange={handleInputChange} />
            <InputField name="credentialSecretKeyVal" label="Credential Secret Key Value" value={cloudInfo.credentialSecretKeyVal} onChange={handleInputChange} />
            <InputField name="regionName" label="Region Name" value={cloudInfo.regionName} onChange={handleInputChange} />
            <InputField name="regionKey" label="Region Key" value={cloudInfo.regionKey} onChange={handleInputChange} />
            <InputField name="regionValue" label="Region Value" value={cloudInfo.regionValue} onChange={handleInputChange} />
            <InputField name="zoneKey" label="Zone Key" value={cloudInfo.zoneKey} onChange={handleInputChange} />
            <InputField name="zoneValue" label="Zone Value" value={cloudInfo.zoneValue} onChange={handleInputChange} />
            <InputField name="configName" label="Config Name" value={cloudInfo.configName} onChange={handleInputChange} />
          </>
        );
      case 'AZURE':
        return (
          <>
            <InputField name="driverName" label="Driver Name" value={cloudInfo.driverName} onChange={handleInputChange} />
            <InputField name="driverLibFileName" label="Driver Lib File Name" value={cloudInfo.driverLibFileName} onChange={handleInputChange} />
            <InputField name="credentialName" label="Credential Name" value={cloudInfo.credentialName} onChange={handleInputChange} />
            <InputField name="clientIdKey" label="Client ID Key" value={cloudInfo.clientIdKey} onChange={handleInputChange} />
            <InputField name="clientIdValue" label="Client ID Value" value={cloudInfo.clientIdValue} onChange={handleInputChange} />
            <InputField name="clientSecretKey" label="Client Secret Key" value={cloudInfo.clientSecretKey} onChange={handleInputChange} />
            <InputField name="clientSecretValue" label="Client Secret Value" value={cloudInfo.clientSecretValue} onChange={handleInputChange} />
            <InputField name="tenantIdKey" label="Tenant ID Key" value={cloudInfo.tenantIdKey} onChange={handleInputChange} />
            <InputField name="tenantIdValue" label="Tenant ID Value" value={cloudInfo.tenantIdValue} onChange={handleInputChange} />
            <InputField name="subscriptionIdKey" label="Subscription ID Key" value={cloudInfo.subscriptionIdKey} onChange={handleInputChange} />
            <InputField name="subscriptionIdValue" label="Subscription ID Value" value={cloudInfo.subscriptionIdValue} onChange={handleInputChange} />
            <InputField name="regionName" label="Region Name" value={cloudInfo.regionName} onChange={handleInputChange} />
            <InputField name="regionKey" label="Region Key" value={cloudInfo.regionKey} onChange={handleInputChange} />
            <InputField name="regionValue" label="Region Value" value={cloudInfo.regionValue} onChange={handleInputChange} />
            <InputField name="zoneKey" label="Zone Key" value={cloudInfo.zoneKey} onChange={handleInputChange} />
            <InputField name="zoneValue" label="Zone Value" value={cloudInfo.zoneValue} onChange={handleInputChange} />
            <InputField name="configName" label="Config Name" value={cloudInfo.configName} onChange={handleInputChange} />
          </>
        );
      case 'OPENSTACK':
        return (
          <>
            <InputField name="driverName" label="Driver Name" value={cloudInfo.driverName} onChange={handleInputChange} />
            <InputField name="driverLibFileName" label="Driver Lib File Name" value={cloudInfo.driverLibFileName} onChange={handleInputChange} />
            <InputField name="credentialName" label="Credential Name" value={cloudInfo.credentialName} onChange={handleInputChange} />
            <InputField name="identityEndpointKey" label="Identity Endpoint Key" value={cloudInfo.identityEndpointKey} onChange={handleInputChange} />
            <InputField name="identityEndpointValue" label="Identity Endpoint Value" value={cloudInfo.identityEndpointValue} onChange={handleInputChange} />
            <InputField name="usernameKey" label="Username Key" value={cloudInfo.usernameKey} onChange={handleInputChange} />
            <InputField name="usernameValue" label="Username Value" value={cloudInfo.usernameValue} onChange={handleInputChange} />
            <InputField name="domainNameKey" label="Domain Name Key" value={cloudInfo.domainNameKey} onChange={handleInputChange} />
            <InputField name="domainNameValue" label="Domain Name Value" value={cloudInfo.domainNameValue} onChange={handleInputChange} />
            <InputField name="passwordKey" label="Password Key" value={cloudInfo.passwordKey} onChange={handleInputChange} />
            <InputField name="passwordValue" label="Password Value" value={cloudInfo.passwordValue} onChange={handleInputChange} />
            <InputField name="projectIDKey" label="Project ID Key" value={cloudInfo.projectIDKey} onChange={handleInputChange} />
            <InputField name="projectIDValue" label="Project ID Value" value={cloudInfo.projectIDValue} onChange={handleInputChange} />
            <InputField name="regionName" label="Region Name" value={cloudInfo.regionName} onChange={handleInputChange} />
            <InputField name="regionKey" label="Region Key" value={cloudInfo.regionKey} onChange={handleInputChange} />
            <InputField name="regionValue" label="Region Value" value={cloudInfo.regionValue} onChange={handleInputChange} />
          </>
        );
      default:
        return null;
    }
  };

  const InputField = ({ name, label, value, onChange }) => (
    <div className="cloud-info-form-group">
      <label>{label}</label>
      <input
        type="text"
        name={name}
        value={value}
        onChange={onChange}
        required
      />
    </div>
  );

  return (
    <div className="cloud-info-container">
      <div className="cloud-info-header">
        <h2>Cloud Information Management</h2>
      </div>
      {message && <div className="cloud-info-message">{message}</div>}
      <div className="cloud-info-table-container">
        <table className="cloud-info-table">
          <thead>
            <tr>
              <th>Provider</th>
              <th>Driver Name</th>
              <th>Region</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {cloudProviders.map(provider => (
              <tr key={provider}>
                <td>{provider}</td>
                <td>{existingCloudInfo[provider]?.driverName || 'N/A'}</td>
                <td>{existingCloudInfo[provider]?.regionValue || 'N/A'}</td>
                <td>
                  {existingCloudInfo[provider] ? (
                    <>
                      <button className="cloud-info-button cloud-info-button-small cloud-info-button-danger" onClick={() => handleDelete(provider)}>Delete</button>
                      <button className="cloud-info-button cloud-info-button-small cloud-info-button-secondary" onClick={() => handleModify(provider)}>Modify</button>
                    </>
                  ) : (
                    <button className="cloud-info-button cloud-info-button-small cloud-info-button-primary" onClick={() => handleCreate(provider)}>Create</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {loading && <div className="cloud-info-loading">Loading...</div>}
      {modalVisible && (
        <div className="cloud-info-modal">
          <div className="cloud-info-modal-content">
            <h3>{isModifying ? 'Modify' : 'Create'} Cloud Provider: {activeProvider}</h3>
            <form onSubmit={handleSubmit}>
              {renderFormFields()}
              <div className="cloud-info-form-actions">
                <button type="submit" className="cloud-info-button cloud-info-button-primary" disabled={loading}>
                  {loading ? 'Processing...' : (isModifying ? 'Update' : 'Create')}
                </button>
                <button type="button" className="cloud-info-button" onClick={() => setModalVisible(false)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default CloudInfo;