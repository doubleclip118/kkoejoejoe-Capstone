import React, { useState } from 'react';

function ConnectionView() {
  const [awsVMs, setAwsVMs] = useState([]);
  const [azureVMs, setAzureVMs] = useState([]);
  const [openstackVMs, setOpenstackVMs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleViewVMsFromDB = async () => {
    setLoading(true);
    setError(null);

    try {
      // Fetch AWS VMs
      const awsResponse = await fetch('http://your-api-ip-address/view-vms/aws', { method: 'GET' });
      if (!awsResponse.ok) throw new Error('Failed to fetch AWS VMs');
      const awsData = await awsResponse.json();
      setAwsVMs(awsData);

      // Fetch Azure VMs
      const azureResponse = await fetch('http://your-api-ip-address/view-vms/azure', { method: 'GET' });
      if (!azureResponse.ok) throw new Error('Failed to fetch Azure VMs');
      const azureData = await azureResponse.json();
      setAzureVMs(azureData);

      // Fetch OpenStack VMs
      const openstackResponse = await fetch('http://your-api-ip-address/view-vms/openstack', { method: 'GET' });
      if (!openstackResponse.ok) throw new Error('Failed to fetch OpenStack VMs');
      const openstackData = await openstackResponse.json();
      setOpenstackVMs(openstackData);
    } catch (error) {
      console.error('Error fetching VMs:', error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h4>저장된 VM 보기</h4>
      <button className="action-button" onClick={handleViewVMsFromDB}>조회</button>

      {loading && <p>로딩 중...</p>}
      {error && <p style={{ color: 'red' }}>에러 발생: {error}</p>}

      {!loading && !error && (
        <>
          <h5>AWS VMs</h5>
          <div>
            {awsVMs.length > 0 ? (
              awsVMs.map(vm => (
                <div key={vm.id}>
                  <p>{vm.vmName} - {vm.status}</p>
                </div>
              ))
            ) : (
              <p>등록된 AWS VM이 없습니다.</p>
            )}
          </div>

          <h5>Azure VMs</h5>
          <div>
            {azureVMs.length > 0 ? (
              azureVMs.map(vm => (
                <div key={vm.id}>
                  <p>{vm.vmName} - {vm.status}</p>
                </div>
              ))
            ) : (
              <p>등록된 Azure VM이 없습니다.</p>
            )}
          </div>

          <h5>OpenStack VMs</h5>
          <div>
            {openstackVMs.length > 0 ? (
              openstackVMs.map(vm => (
                <div key={vm.id}>
                  <p>{vm.vmName} - {vm.status}</p>
                </div>
              ))
            ) : (
              <p>등록된 OpenStack VM이 없습니다.</p>
            )}
          </div>
        </>
      )}
    </div>
  );
}

export default ConnectionView;