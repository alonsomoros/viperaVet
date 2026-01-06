import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Home.css';

const Home = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="home-container">
            <div className="home-card">
                <h1>Bienvenido</h1>
                {user ? (
                    <div className="user-info">
                        <p className="welcome-message">Te has logueado como <strong>{user.role}</strong></p>
                        <p className="user-detail">Usuario: {user.username}</p>
                        <button onClick={handleLogout} className="logout-button">Cerrar Sesión</button>
                    </div>
                ) : (
                    <p>Cargando información del usuario...</p>
                )}
            </div>
        </div>
    );
};

export default Home;
