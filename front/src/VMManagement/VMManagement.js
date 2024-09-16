import React, { useState } from 'react';

function VMManagement() {
  const [vms, setVMs] = useState([]);
  const [menu, setMenu] = useState('');  // 메뉴 상태 추가
  const [newVM, setNewVM] = useState({
    connectionName: '',
    vmName: '',
    vpcName: '',
    vpcIPv4_CIDR: '',
    subnetName: '',
    subnetIPv4_CIDR: '',
    securityGroupName: '',
    securityGroupRules: [{ fromPort: '', toPort: '', ipProtocol: '', direction: '' }],
    keypairName: '',
    imageName: '',
    vmSpec: '',
    regionName: '',
    zoneName: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewVM(prev => ({ ...prev, [name]: value }));
  };

  // 1. VM Connection 생성: 데이터베이스에 VM 정보 저장
  const handleSaveToDB = async () => {
    try {
      const response = await fetch('http://your-api-ip-address/save-vm-info', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newVM),
      });

      if (!response.ok) {
        throw new Error('Failed to save VM information');
      }

      const result = await response.json();
      console.log('VM Info saved:', result);
      alert('VM 정보가 DB에 저장되었습니다.');
    } catch (error) {
      console.error('Error saving VM to DB:', error);
    }
  };

  // 2. VM Connect: 서버에 VM 생성 요청 전송
  const handleCreateVMOnServer = async () => {
    try {
      const response = await fetch('http://your-api-ip-address/create-vm', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newVM),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      console.log('VM created:', result);
      alert('VM이 성공적으로 생성되었습니다.');
    } catch (error) {
      console.error('Error creating VM:', error);
    }
  };

  // 3. Connection View: DB에 저장된 VM 내용 조회
  const handleViewVMsFromDB = async () => {
    try {
      const response = await fetch('http://your-api-ip-address/view-vms', {
        method: 'GET',
      });

      if (!response.ok) {
        throw new Error('Failed to fetch VMs');
      }

      const result = await response.json();
      console.log('VMs fetched:', result);
      setVMs(result);
    } catch (error) {
      console.error('Error fetching VMs:', error);
    }
  };

  // 4. Delete VM: 서버에 VM 삭제 요청 전송
  const handleDeleteVM = async (vmId) => {
    try {
      const response = await fetch(`http://your-api-ip-address/delete-vm/${vmId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Failed to delete VM');
      }

      const result = await response.json();
      console.log('VM deleted:', result);
      alert('VM이 성공적으로 삭제되었습니다.');
    } catch (error) {
      console.error('Error deleting VM:', error);
    }
  };

  return (
    <div className="management-content">
      <h3>Virtual Machine Management</h3>
      
      {/* 4개의 버튼 */}
      <button className="action-button" onClick={() => setMenu('save')}>VM Connection 생성</button>
      <button className="action-button" onClick={() => setMenu('connect')}>VM Connect</button>
      <button className="action-button" onClick={() => setMenu('view')}>Connection View</button>
      <button className="action-button" onClick={() => setMenu('delete')}>Delete VM</button>

      {/* 각 메뉴에 따른 폼 및 기능 표시 */}
      {menu === 'save' && (
        <div>
          <h4>VM 정보를 데이터베이스에 저장</h4>
          <form onSubmit={(e) => { e.preventDefault(); handleSaveToDB(); }}>
            {/* 입력 폼: newVM 상태를 업데이트하는 form */}
            <input type="text" name="connectionName" value={newVM.connectionName} onChange={handleInputChange} placeholder="Connection Name" required />
            {/* 나머지 VM 정보 입력 */}
            <button type="submit" className="action-button">저장</button>
          </form>
        </div>
      )}

      {menu === 'connect' && (
        <div>
          <h4>VM Connect</h4>
          <button className="action-button" onClick={handleCreateVMOnServer}>서버로 VM 생성 요청</button>
        </div>
      )}

      {menu === 'view' && (
        <div>
          <h4>저장된 VM 보기</h4>
          <button className="action-button" onClick={handleViewVMsFromDB}>조회</button>
          <div>
            {vms.map(vm => (
              <div key={vm.id}>
                <p>{vm.vmName} - {vm.status}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {menu === 'delete' && (
        <div>
          <h4>VM 삭제</h4>
          <input type="text" placeholder="VM ID" onChange={(e) => setNewVM({ ...newVM, id: e.target.value })} />
          <button className="action-button" onClick={() => handleDeleteVM(newVM.id)}>삭제</button>
        </div>
      )}
    </div>
  );
}

export default VMManagement;