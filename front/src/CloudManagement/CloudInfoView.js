import React from 'react';

function CloudInfoView({ existingCloudInfo }) {
  return (
    <div className="existing-cloud-info">
      {existingCloudInfo.map((info, index) => (
        <div key={index} className="cloud-info-item">
          <h4>{info.ProviderName}</h4>
          <p>Driver Name: {info.DriverName}</p>
          <p>Credential Name: {info.CredentialName}</p>
          <p>Region: {info.RegionValue}</p>
          <p>Zone: {info.ZoneValue}</p>
        </div>
      ))}
    </div>
  );
}export default CloudInfoView;