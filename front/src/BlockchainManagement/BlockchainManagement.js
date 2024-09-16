import React, { useState, useEffect } from 'react';

function BlockchainManagement() {
  const [networks, setNetworks] = useState([
    { id: 1, name: 'Network-1', nodes: 5 },
    { id: 2, name: 'Network-2', nodes: 3 },
  ]);

  const [vms, setVMs] = useState([]); // VM 리스트
  const [newNetworkName, setNewNetworkName] = useState('');
  const [newNetworkNodes, setNewNetworkNodes] = useState(1);
  const [selectedOrgVM, setSelectedOrgVM] = useState(null); // org로 선택한 VM
  const [selectedCaVM, setSelectedCaVM] = useState(null); // ca로 선택한 VM

  // VM 데이터를 API로부터 가져오기 (예시로 setTimeout 사용)
  useEffect(() => {
    // 실제 API 호출로 대체 필요
    const fetchVMs = async () => {
      // 예시 VM 데이터
      const vmData = [
        { id: 1, name: 'VM-1' },
        { id: 2, name: 'VM-2' },
        { id: 3, name: 'VM-3' },
      ];
      setTimeout(() => setVMs(vmData), 1000); // 1초 후에 VM 데이터 설정
    };

    fetchVMs();
  }, []);

  // 블록체인 네트워크 생성 함수
  const handleCreateNetwork = () => {
    if (!selectedOrgVM || !selectedCaVM) {
      alert('Please select both Org and CA VMs');
      return;
    }

    const newId = networks.length > 0 ? networks[networks.length - 1].id + 1 : 1;
    const newNetwork = { id: newId, name: newNetworkName, nodes: newNetworkNodes, org: selectedOrgVM, ca: selectedCaVM };

    // 네트워크 추가
    setNetworks([...networks, newNetwork]);
    setNewNetworkName('');  // 폼 초기화
    setNewNetworkNodes(1);  // 폼 초기화
    setSelectedOrgVM(null);  // 선택 초기화
    setSelectedCaVM(null);   // 선택 초기화
  };

  // 블록체인 네트워크 삭제 함수
  const handleDeleteNetwork = (id) => {
    const updatedNetworks = networks.filter(network => network.id !== id);
    setNetworks(updatedNetworks);
  };

  return (
    <div className="management-content">
      <h3>Blockchain Network Management</h3>

      {/* 네트워크 리스트 표시 */}
      {networks.map(network => (
        <div key={network.id} className="network-item">
          <span>{network.name} - Nodes: {network.nodes} (Org: {network.org?.name}, CA: {network.ca?.name})</span>
          <button className="action-button" onClick={() => handleDeleteNetwork(network.id)}>Delete</button>
        </div>
      ))}

      {/* 블록체인 네트워크 생성 폼 */}
      <div className="new-network-form">
        <h4>Create New Network</h4>
        <input
          type="text"
          value={newNetworkName}
          onChange={(e) => setNewNetworkName(e.target.value)}
          placeholder="Network Name"
          required
        />
        <input
          type="number"
          value={newNetworkNodes}
          onChange={(e) => setNewNetworkNodes(e.target.value)}
          placeholder="Number of Nodes"
          min="1"
          required
        />

        {/* VM 리스트에서 Org 선택 */}
        <div>
          <h5>Select Org VM</h5>
          <select onChange={(e) => setSelectedOrgVM(vms.find(vm => vm.id === parseInt(e.target.value)))}>
            <option value="">Select Org VM</option>
            {vms.map(vm => (
              <option key={vm.id} value={vm.id}>{vm.name}</option>
            ))}
          </select>
        </div>

        {/* VM 리스트에서 CA 선택 */}
        <div>
          <h5>Select CA VM</h5>
          <select onChange={(e) => setSelectedCaVM(vms.find(vm => vm.id === parseInt(e.target.value)))}>
            <option value="">Select CA VM</option>
            {vms.map(vm => (
              <option key={vm.id} value={vm.id}>{vm.name}</option>
            ))}
          </select>
        </div>

        <button className="action-button" onClick={handleCreateNetwork}>Create Network</button>
      </div>
    </div>
  );
}

export default BlockchainManagement;