import React, { useState } from 'react';
import './Main.css';
import pusanLogo from './pusan.png';
import CloudInfo from './CloudInfo';
import VMManagement from './VMManagement/VMManagement';
import BlockchainManagement from './BlockchainManagement/BlockchainManagement';

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
      <div className="navbar">
        <div className="navbar-left"> 
          <h2 style={{ color: '#fff'}}>Dashboard</h2>
        </div>
        <div className="navbar-menu">
          <button
            onClick={() => setActiveTab('cloud')}
            className={activeTab === 'cloud' ? 'active' : ''}
          >
            Cloud Info
          </button>
          <button
            onClick={() => setActiveTab('vm')}
            className={activeTab === 'vm' ? 'active' : ''}
          >
            VM Management
          </button>
          <button
            onClick={() => setActiveTab('blockchain')}
            className={activeTab === 'blockchain' ? 'active' : ''}
          >
            Blockchain Network
          </button>
        </div>
      </div>
      <div className="content">
        <h2>Cloud Management Dashboard</h2>
        <div className="tab-content">
          {renderContent()}
        </div>
      </div>
    </div>
  );
}

export default Main;
