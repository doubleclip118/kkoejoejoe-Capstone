import React, { useState } from 'react';
import './Main.css';
import pusanLogo from './pusan.png';
import CloudInfo from './CloudInfo';
import VMManagement from './VMManagement';
import BlockchainManagement from './BlockchainManagement';

function Main() {
  const [activeTab, setActiveTab] = useState('cloud');

  const renderContent = () => {
    switch (activeTab) {
      case 'cloud':
        return <CloudInfo />;
      case 'vm':
        return <VMManagement />;
      case 'blockchain':
        return <BlockchainManagement />;
      default:
        return <CloudInfo />;
    }
  };

  return (
    <div className="App">
      <div className="card">
        <img src={pusanLogo} alt="Pusan Logo" className="logo" />
        <h2>Cloud Management Dashboard</h2>
        <div className="tab-buttons">
          <button onClick={() => setActiveTab('cloud')}>Cloud Info</button>
          <button onClick={() => setActiveTab('vm')}>VM Management</button>
          <button onClick={() => setActiveTab('blockchain')}>Blockchain Network</button>
        </div>
        {renderContent()}
      </div>
    </div>
  );
}

export default Main;