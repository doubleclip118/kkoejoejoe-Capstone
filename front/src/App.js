import React, { useState } from 'react';
import styles from './App.css';
import pusanLogo from './pusan.png';
import CloudInfo from './CloudInfo'; // CloudInfo 컴포넌트를 import 합니다.
import Main from './Main';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태를 관리하는 상태를 추가합니다.
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const handleSwitch = () => {
    setIsLogin(!isLogin);
    setError('');
    setSuccessMessage('');
    setUsername('');
    setPassword('');
    setConfirmPassword('');
  };

  const handleLogin = async () => {
    try {
      const response = await fetch('http://192.168.20.38:8080/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      const data = await response.json();
      console.log('Login successful:', data);
      setSuccessMessage('Login successful!');
      localStorage.setItem('userId', data.id); // userId를 local storage에 저장합니다.
      setIsLoggedIn(true); // 로그인 성공 시 상태를 변경합니다.
      // 여기서 로그인 성공 후 처리 (예: 토큰 저장, 리다이렉트 등)
    } catch (error) {
      setError('Login failed: ' + error.message);
    }
  };

  const handleSignup = async () => {
    try {
      const response = await fetch('http://192.168.20.38:8080/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Signup failed');
      }

      const data = await response.json();
      console.log('Signup successful:', data);
      setSuccessMessage('Signup successful!');
      localStorage.setItem('userId', data.id); // userId를 local storage에 저장합니다.
      setIsLoggedIn(true); // 회원가입 성공 시 상태를 변경합니다.
      // 여기서 회원가입 성공 후 처리
    } catch (error) {
      setError('Signup failed: ' + error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    if (!username || !password || (!isLogin && !confirmPassword)) {
      setError('All fields are required');
      return;
    }
    if (!isLogin && password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (isLogin) {
      await handleLogin();
    } else {
      await handleSignup();
    }
  };

  const handleTestLogin = () => {
    setIsLoggedIn(true);
  };

  if (isLoggedIn) {
    return <Main />;
  }

  return (        <div className="App">
    <div className="card">
      <img src={pusanLogo} alt="Pusan Logo" className="logo" />
      <h2>{isLogin ? 'Login' : 'Signup'}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter your username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder={isLogin ? "Enter your password" : "Create a password"}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {!isLogin && (
          <input
            type="password"
            placeholder="Confirm your password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        )}
        {error && <p className="error">{error}</p>}
        {successMessage && <p className="success">{successMessage}</p>}
        <button type="submit">{isLogin ? 'Login' : 'Signup'}</button>
      </form>
        {<button onClick={handleTestLogin}>Test Login</button>}
      <p className="switch-text">
        {isLogin ? "Don't have an account? " : "Already have an account? "}
        <a href="#" onClick={handleSwitch}>{isLogin ? 'Signup' : 'Login'}</a>
      </p>
    </div>
  </div>
  );
}

export default App;
