import React from 'react';

function CloudProviderSelect({ cloudProvider, setCloudProvider }) {
  return (
    <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)}>
      <option value="AWS">AWS</option>
      <option value="AZURE">Azure</option>
    </select>
  );
}

export default CloudProviderSelect;