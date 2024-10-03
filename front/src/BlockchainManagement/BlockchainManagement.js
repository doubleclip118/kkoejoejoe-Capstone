import React, { useState } from 'react';
import NetworkCreation from './NetworkCreation';
import ContractCreation from './ContractCreation';
import ContractDeployment from './ContractDeployment';
import ChaincodeQuery from './ChaincodeQuery';
import ChaincodeInvoke from './ChaincodeInvoke';

function BlockchainManagement() {
  const [selectedFunction, setSelectedFunction] = useState('network');

  // 각 기능을 선택하면 해당 컴포넌트가 렌더링됨
  const renderSelectedFunction = () => {
    switch (selectedFunction) {
      case 'network':
        return <NetworkCreation />;
      case 'contractCreation':
        return <ContractCreation />;
      case 'contractDeployment':
        return <ContractDeployment />;
      case 'chaincodeQuery':
        return <ChaincodeQuery />;
      case 'chaincodeInvoke':
        return <ChaincodeInvoke />;
      default:
        return <NetworkCreation />;
    }
  };

  return (
    <div className="management-content">
      <h3>Blockchain Management</h3>
      <div className="tabs">
        <button onClick={() => setSelectedFunction('network')}>Network Creation</button>
        <button onClick={() => setSelectedFunction('contractCreation')}>Smart Contract</button>
      </div>

      {/* 선택한 기능을 렌더링 */}
      <div className="function-content">
        {renderSelectedFunction()}
      </div>
    </div>
  );
}

export default BlockchainManagement;
