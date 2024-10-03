// CloudInfo.js
import React, { useState, useEffect } from 'react';
import './Main.css';
import CloudInfoView from './CloudInfoView';
import CloudInfoForm from './CloudInfoForm';
import CloudInfoDelete from './CloudInfoDelete';
import CloudInfoNav from './CloudInfoNav';
import Connect from './Connect';
import ViewConnection from './ViewConnection';
import DeleteConnection from './CloudManagement/DeleteConnection';

function CloudInfo() {
  const [menu, setMenu] = useState('view');
  const [cloudProvider, setCloudProvider] = useState('AWS');
  const [existingCloudInfo, setExistingCloudInfo] = useState([]);

  useEffect(() => {
    fetchExistingCloudInfo();
  }, []);

  const fetchExistingCloudInfo = async () => {
    try {
      // cloudProvider와 id 값을 사용하여 URL을 동적으로 구성
      const userId = parseInt(localStorage.getItem('userId'))
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
  

  return (
    <div className="management-content">
      <h2>Cloud Information Management</h2>
      <CloudInfoNav setMenu={setMenu} />

      {menu === 'connect' &&<Connect 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
        />}
      {menu === 'view_connection' &&<ViewConnection/>}
      {menu === 'delete_connection' && <DeleteConnection/>}
      {menu === 'view_information' && <CloudInfoView existingCloudInfo={existingCloudInfo} />}

      {menu === 'create' && (
        <CloudInfoForm 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
          formType="create"
        />
      )}

      {menu === 'delete' && (
        <CloudInfoDelete 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
        />
      )}

      {menu === 'update' && (
        <CloudInfoForm 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
          formType="update"
        />
      )}
    </div>
  );
}

export default CloudInfo;