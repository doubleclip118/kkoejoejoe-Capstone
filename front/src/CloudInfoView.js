import React from 'react';

const CloudInfoView = ({ cloudInfo }) => {
  if (!cloudInfo) {
    return <p>No information available.</p>;
  }

  return (
    <div className="cloud-info-view">
      <h4>{cloudInfo.providerName} Information</h4>
      <table>
        <tbody>
          <tr><td>Driver Name:</td><td>{cloudInfo.driverName}</td></tr>
          <tr><td>Driver Lib File Name:</td><td>{cloudInfo.driverLibFileName}</td></tr>
          <tr><td>Credential Name:</td><td>{cloudInfo.credentialName}</td></tr>
          <tr><td>Region Name:</td><td>{cloudInfo.regionName}</td></tr>
          <tr><td>Region:</td><td>{cloudInfo.regionValue}</td></tr>
          <tr><td>Zone:</td><td>{cloudInfo.zoneValue}</td></tr>
          <tr><td>Config Name:</td><td>{cloudInfo.configName}</td></tr>
        </tbody>
      </table>
    </div>
  );
};

export default CloudInfoView;