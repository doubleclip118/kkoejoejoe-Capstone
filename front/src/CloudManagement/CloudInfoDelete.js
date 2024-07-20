import React from 'react';
import CloudProviderSelect from './CloudProviderSelect';

function CloudInfoDelete({ cloudProvider, setCloudProvider, onDelete }) {
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
      onDelete();
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div>
      <CloudProviderSelect 
        cloudProvider={cloudProvider}
        setCloudProvider={setCloudProvider}
      />
      <button onClick={handleDelete} className="action-button">Delete Cloud Info</button>
    </div>
  );
}

export default CloudInfoDelete;