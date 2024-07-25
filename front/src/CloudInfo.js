// CloudInfo.js
import React, { useState, useEffect } from 'react';
import './Main.css';
import CloudInfoView from './CloudInfoView';
import CloudInfoForm from './CloudInfoForm';
import CloudInfoDelete from './CloudInfoDelete';
import CloudInfoNav from './CloudInfoNav';
import Connect from './Connect';

function CloudInfo() {
  const [menu, setMenu] = useState('view');
  const [cloudProvider, setCloudProvider] = useState('AWS');
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

  return (
    <div className="management-content">
      <h2>Cloud Information Management</h2>
      <CloudInfoNav setMenu={setMenu} />

      {menu === 'connect && <Connect/>' &&<Connect 
          cloudProvider={cloudProvider} 
          setCloudProvider={setCloudProvider}
          fetchExistingCloudInfo={fetchExistingCloudInfo}
        />}
      {menu === 'view_connection'}
      {menu === 'delete_connection'}
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