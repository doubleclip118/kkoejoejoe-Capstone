import React, { useState } from "react";

function ViewConnection() {
  const [cloudProvider, setCloudProvider] = useState("AWS");
  const [isConnecting, setIsConnecting] = useState(false);
  const [error, setError] = useState(null);
  const [connectionStatus, setConnectionStatus] = useState(null);
  const [cloudInfo, setCloudInfo] = useState(null);  // Cloud 정보 저장

  const connectToCloudProvider = async () => {
    setIsConnecting(true);
    setError(null);
    setConnectionStatus(null);
    setCloudInfo(null);  // 새로운 연결 시 기존 정보 초기화

    try {
      const userId = localStorage.getItem('userId');
      
      // userId를 URL의 {id} 부분에 포함시킴
      const response = await fetch(`http://192.168.20.38:8080/api/cloud/${cloudProvider.toLowerCase()}/${userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to connect to cloud provider');
      }

      const result = await response.json();
      setCloudInfo(result);  // Cloud 정보 저장

      // Check if the response includes userId to determine successful connection
      if (result.userId) {
        setConnectionStatus(`Successfully connected to ${cloudProvider}`);
      } else {
        throw new Error('Invalid response from server');
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setIsConnecting(false);
    }
  };

  // 클라우드 정보 표시를 클라우드 제공자에 따라 다르게 처리
  const renderCloudInfo = () => {
    if (!cloudInfo) return null;

    switch (cloudProvider) {
      case "AWS":
        return (
          <div className="cloud-info-details">
            <h4>Cloud Provider: AWS</h4>
            <p>Provider Name: {cloudInfo.providerName}</p>
            <p>Driver Name: {cloudInfo.driverName}</p>
            <p>Credential Name: {cloudInfo.credentialName}</p>
            <p>Access Key: {cloudInfo.credentialAccessKey}</p>
            <p>Access Key Value: {cloudInfo.credentialAccessKeyVal}</p>
            <p>Secret Key: {cloudInfo.credentialSecretKey}</p>
            <p>Secret Key Value: {cloudInfo.credentialSecretKeyVal}</p>
            <p>Region: {cloudInfo.regionValue}</p>
            <p>Zone: {cloudInfo.zoneValue}</p>
          </div>
        );
      case "AZURE":
        return (
          <div className="cloud-info-details">
            <h4>Cloud Provider: AZURE</h4>
            <p>Provider Name: {cloudInfo.providerName}</p>
            <p>Driver Name: {cloudInfo.driverName}</p>
            <p>Credential Name: {cloudInfo.credentialName}</p>
            <p>Client ID: {cloudInfo.clientIdKey} - {cloudInfo.clientIdValue}</p>
            <p>Client Secret: {cloudInfo.clientSecretKey} - {cloudInfo.clientSecretValue}</p>
            <p>Tenant ID: {cloudInfo.tenantIdKey} - {cloudInfo.tenantIdValue}</p>
            <p>Subscription ID: {cloudInfo.subscriptionIdKey} - {cloudInfo.subscriptionIdValue}</p>
            <p>Region: {cloudInfo.regionValue}</p>
            <p>Zone: {cloudInfo.zoneValue}</p>
          </div>
        );
      case "OPENSTACK":
        return (
          <div className="cloud-info-details">
            <h4>Cloud Provider: OPENSTACK</h4>
            <p>Provider Name: {cloudInfo.providerName}</p>
            <p>Driver Name: {cloudInfo.driverName}</p>
            <p>Credential Name: {cloudInfo.credentialName}</p>
            <p>Identity Endpoint: {cloudInfo.identityEndpointKey} - {cloudInfo.identityEndpointValue}</p>
            <p>Username: {cloudInfo.usernameKey} - {cloudInfo.usernameValue}</p>
            <p>Domain Name: {cloudInfo.domainNameKey} - {cloudInfo.domainNameValue}</p>
            <p>Password: {cloudInfo.passwordKey} - {cloudInfo.passwordValue}</p>
            <p>Project ID: {cloudInfo.projectIDKey} - {cloudInfo.projectIDValue}</p>
            <p>Region: {cloudInfo.regionValue}</p>
            <p>Zone: {cloudInfo.zoneValue}</p>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="cloud-info-view">
      <h3>View Cloud Connection</h3>
      <select
        value={cloudProvider}
        onChange={(e) => setCloudProvider(e.target.value)}
        disabled={isConnecting}
      >
        <option value="AWS">AWS</option>
        <option value="AZURE">Azure</option>
        <option value="OPENSTACK">Openstack</option>
      </select>
      <button
        onClick={connectToCloudProvider}
        className="action-button view-connection-button"
        disabled={isConnecting}
      >
        {isConnecting ? 'Viewing...' : 'View Cloud Connection'}
      </button>

      {/* 오류 메시지 */}
      {error && <p className="error-message">{error}</p>}

      {/* 성공 메시지 */}
      {connectionStatus && <p className="success-message">{connectionStatus}</p>}

      {/* Cloud 정보 표시 */}
      {renderCloudInfo()}
    </div>
  );
}

export default ViewConnection;
