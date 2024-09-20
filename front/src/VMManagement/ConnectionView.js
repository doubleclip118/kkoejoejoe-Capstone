import React, { useState } from 'react';

function ConnectionView() {
  const [vms, setVMs] = useState([]);

  const handleViewVMsFromDB = async () => {
    try {
      const response = await fetch('http://your-api-ip-address/view-vms', { method: 'GET' });

      if (!response.ok) throw new Error('Failed to fetch VMs');

      const result = await response.json();
      setVMs(result);
    } catch (error) {
      console.error('Error fetching VMs:', error);
    }
  };

  return (
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
  );
}

export default ConnectionView;