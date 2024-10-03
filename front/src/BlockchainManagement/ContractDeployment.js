import React, { useState } from 'react';

function ContractDeployment() {
  const [ccName, setCcName] = useState(''); // 체인코드 이름
  const [ccPath, setCcPath] = useState(''); // 체인코드 경로
  const [ccLanguage, setCcLanguage] = useState('go'); // 체인코드 언어 (기본 값: Go)

  const handleDeployContract = async () => {
    // 스마트 컨트랙트 배포 로직
    try {
      const response = await fetch('http://192.168.20.2/fabric/dashboard/deployCC', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          cc_name: ccName,
          cc_path: ccPath,
          cc_language: ccLanguage,
        }),
      });

      if (!response.ok) throw new Error('Failed to deploy contract');
      
      const data = await response.json();
      alert(`Smart contract deployed successfully: ${data.description}`);

      // 폼 초기화
      setCcName('');
      setCcPath('');
      setCcLanguage('go');
    } catch (error) {
      console.error('Error deploying contract:', error);
      alert(`Smart contract deployment failed: ${error.message}`);
    }
  };

  return (
    <div>
      <h4>Deploy Smart Contract</h4>
      <input
        type="text"
        value={ccName}
        onChange={(e) => setCcName(e.target.value)}
        placeholder="Chaincode Name"
      />
      <input
        type="text"
        value={ccPath}
        onChange={(e) => setCcPath(e.target.value)}
        placeholder="Chaincode Path"
      />
      <select value={ccLanguage} onChange={(e) => setCcLanguage(e.target.value)}>
        <option value="go">Go</option>
        <option value="node">Node.js</option>
        <option value="java">Java</option>
      </select>
      <button onClick={handleDeployContract}>Deploy Contract</button>
    </div>
  );
}

export default ContractDeployment;
