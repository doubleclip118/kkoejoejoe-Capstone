import React, { useState } from 'react';

function BlockchainManagement() {
  const [networks, setNetworks] = useState([
    { id: 1, name: 'Network-1', nodes: 5 },
    { id: 2, name: 'Network-2', nodes: 3 },
  ]);

  return (
    <div className="management-content">
      <h3>Blockchain Network Management</h3>
      {networks.map(network => (
        <div key={network.id} className="network-item">
          <span>{network.name} - Nodes: {network.nodes}</span>
          <button className="action-button">View Details</button>
        </div>
      ))}
      <button className="action-button">Create New Network</button>
    </div>
  );
}

export default BlockchainManagement;