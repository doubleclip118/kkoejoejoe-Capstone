import React, { useState, useEffect } from 'react';

function VMConnectionForm() {
  const [cloudProvider, setCloudProvider] = useState('aws');
  const [newVM, setNewVM] = useState({
    userId: 1,
    connectionName: '',
    vmName: '',
    vpcName: '',
    vpcIPv4Cidr: '',
    subnetName: '',
    subnetIPv4Cidr: '',
    securityGroupName: '',
    securityGroupRules: [{ fromPort: '', toPort: '', ipProtocol: '', direction: '' }],
    keypairName: '',
    imageName: '',
    vmSpec: '',
    // Azure specific fields
    regionName: '',
    zoneName: '',
    // OpenStack specific fields
    imageType: '',
    vmSpecName: '',
    rootDiskType: '',
    dataDiskNames: [],
    vmUserId: '',
    vmUserPasswd: '',
  });
  const [vmId, setVmId] = useState(''); // vmId를 저장하는 상태
  const [loading, setLoading] = useState(false);  // <-- 여기서 로딩 상태와 setLoading 함수 선언


  useEffect(() => {
    // Reset form when cloud provider changes
    setNewVM(prev => ({
      ...prev,
      regionName: cloudProvider === 'azure' ? prev.regionName : '',
      zoneName: cloudProvider === 'azure' ? prev.zoneName : '',
      imageType: cloudProvider === 'openstack' ? prev.imageType : '',
      vmSpecName: cloudProvider === 'openstack' ? prev.vmSpecName : '',
      rootDiskType: cloudProvider === 'openstack' ? prev.rootDiskType : '',
      dataDiskNames: cloudProvider === 'openstack' ? prev.dataDiskNames : [],
      vmUserId: cloudProvider === 'openstack' ? prev.vmUserId : '',
      vmUserPasswd: cloudProvider === 'openstack' ? prev.vmUserPasswd : '',
    }));
  }, [cloudProvider]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewVM(prev => ({ ...prev, [name]: value }));
  };

  const handleCloudProviderChange = (e) => {
    setCloudProvider(e.target.value);
  };

  const handleSecurityGroupRuleChange = (index, e) => {
    const { name, value } = e.target;
    const updatedRules = [...newVM.securityGroupRules];
    updatedRules[index] = { ...updatedRules[index], [name]: value };
    setNewVM(prev => ({ ...prev, securityGroupRules: updatedRules }));
  };

  const addSecurityGroupRule = () => {
    setNewVM(prev => ({
      ...prev,
      securityGroupRules: [...prev.securityGroupRules, { fromPort: '', toPort: '', ipProtocol: '', direction: '' }]
    }));
  };

  const handleSaveToDB = async () => {
    let apiEndpoint = '';
    switch (cloudProvider) {
      case 'aws':
        apiEndpoint = 'http://192.168.20.38:8080/api/vm/aws';
        break;
      case 'azure':
        apiEndpoint = 'http://192.168.20.38:8080/api/vm/azure';
        break;
      case 'openstack':
        apiEndpoint = 'http://192.168.20.38:8080/api/vm/openstack';
        break;
      default:
        alert('클라우드 제공자를 선택해주세요.');
        return;
    }
    setLoading(true);

    try {
      // 요청 시작 시 5분(300000 ms) 동안 기다리도록 설정
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 300000);
  
      const response = await fetch(apiEndpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newVM),
        signal: controller.signal // 타임아웃 시 요청 취소
      });
  
      clearTimeout(timeoutId);
  
      if (!response.ok) {
        throw new Error('Failed to save VM information');
      }
  
      const result = await response.json();
      alert('VM 정보가 DB에 저장되었습니다.');
  
      // VM 생성 후 받은 vmId를 이용해 생성 요청
      const vmId = result.vmId;
      const vmCreateResponse = await fetch(`http://192.168.20.38:8080/api/vm/${cloudProvider}/con/${vmId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
  
      if (!vmCreateResponse.ok) {
        throw new Error('VM 생성 실패');
      }
  
      alert('VM 생성 성공!');
    } catch (error) {
      if (error.name === 'AbortError') {
        alert('요청이 너무 오래 걸려서 중단되었습니다.');
      } else {
        alert(`에러 발생: ${error.message}`);
      }
    } finally {
      setLoading(false);
    }
  };

  const createVMOnServer = async (vmId) => {
    if (!vmId) {
      alert('vmId가 없습니다. 저장된 VM 정보에서 vmId를 가져오세요.');
      return;
    }

    const apiEndpoint = `http://192.168.20.38:8080/api/vm/${cloudProvider}/con/${vmId}`;

    try {
      const response = await fetch(apiEndpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) throw new Error('Failed to create VM on server');

      const result = await response.json();
      alert('VM이 성공적으로 생성되었습니다.');
    } catch (error) {
      console.error('Error creating VM on server:', error);
    }
  };

  const renderCommonFields = () => (
    <>
      <input type="text" name="connectionName" value={newVM.connectionName} onChange={handleInputChange} placeholder="Connection Name" required />
      <input type="text" name="vmName" value={newVM.vmName} onChange={handleInputChange} placeholder="VM Name" required />
      <input type="text" name="vpcName" value={newVM.vpcName} onChange={handleInputChange} placeholder="VPC Name" required />
      <input type="text" name="vpcIPv4Cidr" value={newVM.vpcIPv4Cidr} onChange={handleInputChange} placeholder="VPC IPv4 CIDR" required />
      <input type="text" name="subnetName" value={newVM.subnetName} onChange={handleInputChange} placeholder="Subnet Name" required />
      <input type="text" name="subnetIPv4Cidr" value={newVM.subnetIPv4Cidr} onChange={handleInputChange} placeholder="Subnet IPv4 CIDR" required />
      <input type="text" name="securityGroupName" value={newVM.securityGroupName} onChange={handleInputChange} placeholder="Security Group Name" required />
      <input type="text" name="keypairName" value={newVM.keypairName} onChange={handleInputChange} placeholder="Keypair Name" required />
      <input type="text" name="imageName" value={newVM.imageName} onChange={handleInputChange} placeholder="Image Name" required />
    </>
  );

  const renderSecurityGroupRules = () => (
    <div>
      <h5>Security Group Rules</h5>
      {newVM.securityGroupRules.map((rule, index) => (
        <div key={index}>
          <input type="text" name="fromPort" value={rule.fromPort} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="From Port" />
          <input type="text" name="toPort" value={rule.toPort} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="To Port" />
          <input type="text" name="ipProtocol" value={rule.ipProtocol} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="IP Protocol" />
          <input type="text" name="direction" value={rule.direction} onChange={(e) => handleSecurityGroupRuleChange(index, e)} placeholder="Direction" />
        </div>
      ))}
      <button type="button" onClick={addSecurityGroupRule}>Add Rule</button>
    </div>
  );

  const renderAWSFields = () => (
    <>
      {renderCommonFields()}
      <input type="text" name="vmSpec" value={newVM.vmSpec} onChange={handleInputChange} placeholder="VM Spec" required />
      {renderSecurityGroupRules()}
    </>
  );

  const renderAzureFields = () => (
    <>
      {renderCommonFields()}
      <input type="text" name="vmSpec" value={newVM.vmSpec} onChange={handleInputChange} placeholder="VM Spec" required />
      <input type="text" name="regionName" value={newVM.regionName} onChange={handleInputChange} placeholder="Region Name" required />
      <input type="text" name="zoneName" value={newVM.zoneName} onChange={handleInputChange} placeholder="Zone Name" required />
      {renderSecurityGroupRules()}
    </>
  );

  const renderOpenStackFields = () => (
    <>
      {renderCommonFields()}
      <input type="text" name="imageType" value={newVM.imageType} onChange={handleInputChange} placeholder="Image Type" required />
      <input type="text" name="vmSpecName" value={newVM.vmSpecName} onChange={handleInputChange} placeholder="VM Spec Name" required />
      <input type="text" name="rootDiskType" value={newVM.rootDiskType} onChange={handleInputChange} placeholder="Root Disk Type" required />
      <input type="text" name="keypairName" value={newVM.keypairName} onChange={handleInputChange} placeholder="Keypair Name" required />
      <input type="text" name="vmUserId" value={newVM.vmUserId} onChange={handleInputChange} placeholder="VM User ID" required />
      <input type="password" name="vmUserPasswd" value={newVM.vmUserPasswd} onChange={handleInputChange} placeholder="VM User Password" required />
      {renderSecurityGroupRules()}
    </>
  );

  return (
    <div>
      <h4>VM 정보를 데이터베이스에 저장</h4>
      <label>클라우드 제공자 선택:</label>
      <select value={cloudProvider} onChange={handleCloudProviderChange}>
        <option value="aws">AWS</option>
        <option value="azure">Azure</option>
        <option value="openstack">OpenStack</option>
      </select>

      <form onSubmit={(e) => { e.preventDefault(); handleSaveToDB(); }}>
        {cloudProvider === 'aws' && renderAWSFields()}
        {cloudProvider === 'azure' && renderAzureFields()}
        {cloudProvider === 'openstack' && renderOpenStackFields()}
        <button type="submit" className="action-button">저장</button>
      </form>
    </div>
  );
}

export default VMConnectionForm;
