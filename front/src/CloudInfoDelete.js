// CloudInfoDelete.js
import React from 'react';

function CloudInfoDelete({ cloudProvider, setCloudProvider, fetchExistingCloudInfo }) {
  const handleDelete = async () => {
    // ... (기존 handleDelete 함수 내용)
  };

  return (
    <div>
      <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)}>
        <option value="AWS">AWS</option>
        <option value="AZURE">Azure</option>
      </select>
      <button onClick={handleDelete} className="action-button">Delete Cloud Info</button>
    </div>
  );
}

export default CloudInfoDelete;