import React, { useState, useEffect } from 'react';
import './Main.css';
import CloudInfoView from './CloudInfoView';
import CloudInfoForm from './CloudInfoForm';
import CloudInfoDelete from './CloudInfoDelete';
import CloudInfoNav from './CloudInfoNav';
import ViewConnection from './ViewConnection';
import DeleteConnection from './CloudManagement/DeleteConnection';
import IntegratedCloudConnectForm from './IntegratedCloudConnectForm';

function CloudInfo() {
  const [menu, setMenu] = useState('create_and_connect');
  const [cloudProvider, setCloudProvider] = useState('AWS');
  const [existingCloudInfo, setExistingCloudInfo] = useState([]);

  useEffect(() => {
    fetchExistingCloudInfo();
  }, [cloudProvider]);

  const fetchExistingCloudInfo = async () => {
    try {
      const userId = parseInt(localStorage.getItem('userId'), 10);
      const url = `http://192.168.20.38:8080/api/cloud/${cloudProvider.toLowerCase()}/${userId}`;
      
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      if (!response.ok) {
        throw new Error('Failed to get cloud info');
      }
  
      const data = await response.json();
      setExistingCloudInfo(data);
    } catch (error) {
      console.error('Error fetching cloud info:', error);
    }
  };

  const renderContent = () => {
    switch (menu) {
      case 'create_and_connect':
        return <IntegratedCloudConnectForm 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
        />;
      case 'view_connection':
        return <ViewConnection />;
      case 'delete_connection':
        return <DeleteConnection />;
      case 'view_information':
        return <CloudInfoView existingCloudInfo={existingCloudInfo} />;
      case 'delete':
        return <CloudInfoDelete 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
        />;
      case 'update':
        return <CloudInfoForm 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
          formType="update"
        />;
      default:
        return null;
    }
  };

  return (
    <div className="management-content">
      <h2>Cloud Information Management</h2>
      <CloudInfoNav setMenu={setMenu} />
      {renderContent()}
    </div>
  );
}

export default CloudInfo;