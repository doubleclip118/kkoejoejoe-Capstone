import React, { useState, useEffect } from 'react';

function DeleteVM({}) {
  const [csp, setCsp] = useState('');
  const [vmList, setVmList] = useState([]);
  const [selectedVmId, setSelectedVmId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const userId = parseInt(localStorage.getItem('userId'));

  // CSP 변경 시 VM 목록 가져오기
  useEffect(() => {
    const fetchVmList = async () => {
      if (!csp) return;

      setLoading(true);
      setError(null);

      try {
        const response = await fetch(`http://192.168.20.38:8080/api/vm/${csp}/con/${userId}`, {
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
  }, [csp, userId]);

  const handleDeleteVM = async () => {
    if (!csp || !selectedVmId) {
      alert('CSP와 VM을 선택하세요.');
      return;
    }

    try {
      const response = await fetch(`http://192.168.20.38:8080/api/vm/${csp}/con/${selectedVmId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) {
        throw new Error('Failed to delete VM');
      }

      const result = await response.text();  // 텍스트 응답을 받음
      if (result === '성공') {
        alert('VM이 성공적으로 삭제되었습니다.');
      } else {
        throw new Error('Unexpected response: ' + result);
      }

    } catch (error) {
      console.error('Error deleting VM:', error);
      setError(error.message);
    }
  };

  return (
    <div>
      <h4>VM 삭제</h4>

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
              <label htmlFor="vm-select">삭제할 VM 선택:</label>
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

          <button className="action-button" onClick={handleDeleteVM}>
            삭제
          </button>
        </>
      )}

      {error && <p style={{ color: 'red' }}>에러 발생: {error}</p>}
    </div>
  );
}

export default DeleteVM;
