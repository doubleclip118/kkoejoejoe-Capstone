import React, { useState, useEffect } from 'react';

function VMConnect({ userid }) {
  const [csp, setCsp] = useState('');
  const [vmList, setVmList] = useState([]);
  const [selectedVmId, setSelectedVmId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // CSP 변경 시 VM 목록 가져오기
  useEffect(() => {
    const fetchVmList = async () => {
      if (!csp) return;

      setLoading(true);
      setError(null);

      try {
        const response = await fetch(`/api/vm/${csp}/con/${userid}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        setVmList(data);  // VM 목록을 상태로 저장
      } catch (error) {
        console.error('Error fetching VM list:', error);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVmList();
  }, [csp, userid]);

  const handleCreateVMOnServer = async () => {
    if (!csp || !selectedVmId) {
      alert('CSP와 VM을 선택하세요.');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await fetch(`/api/vm/${csp}/con/${selectedVmId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      alert('VM이 성공적으로 생성되었습니다.');
    } catch (error) {
      console.error('Error creating VM:', error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h4>VM Connect</h4>

      <label htmlFor="csp-select">클라우드 서비스 제공자 (CSP) 선택:</label>
      <select id="csp-select" value={csp} onChange={(e) => setCsp(e.target.value)}>
        <option value="">CSP를 선택하세요</option>
        <option value="aws">AWS</option>
        <option value="azure">Azure</option>
        <option value="openstack">OpenStack</option>
      </select>

      {loading ? (
        <p>로딩 중...</p>
      ) : (
        <>
          {vmList.length > 0 && (
            <>
              <label htmlFor="vm-select">생성할 VM 선택:</label>
              <select
                id="vm-select"
                value={selectedVmId}
                onChange={(e) => setSelectedVmId(e.target.value)}
              >
                <option value="">VM을 선택하세요</option>
                {vmList.map((vm) => (
                  <option key={vm.vmId} value={vm.vmId}>
                    {vm.vmName} (IP: {vm.ip})
                  </option>
                ))}
              </select>
            </>
          )}

          <button className="action-button" onClick={handleCreateVMOnServer}>
            서버로 VM 생성 요청
          </button>
        </>
      )}

      {error && <p style={{ color: 'red' }}>에러 발생: {error}</p>}
    </div>
  );
}

export default VMConnect;